package com.swagswap.web.springmvc.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swagswap.domain.SwagItem;
import com.swagswap.exceptions.ImageTooLargeException;
import com.swagswap.exceptions.LoadImageFromURLException;
import com.swagswap.service.SwagItemService;

@Controller
public class MailController {
	private static final Logger log = Logger.getLogger(MailController.class);
	// This email has to be registered as an admin in the appengine project you
	// deploy to or it won't allow sending from this address
	private static final String ADMIN_EMAIL = "gaespringstarterapp@gmail.com";

	@Autowired
	private SwagItemService itemService;

	/**
	 * Handle incoming email see
	 * http://code.google.com/appengine/docs/java/mail/receiving.html
	 * 
	 * Create an item from the email and notify the sender of item creation
	 * 
	 */
	@RequestMapping(value = "/_ah/mail/{address}", method = RequestMethod.POST)
	public void createSwagItemFromIncomingEmail(@PathVariable("address")
	String address, HttpServletRequest request, HttpServletResponse response) {
		String fromEmail = null;
		try {
			// Create MimeMessage from request
			// From Appengine docs:
			// http://code.google.com/appengine/docs/java/mail/receiving.html
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			MimeMessage mimeMessage = new MimeMessage(session, request
					.getInputStream());

			String mailSubject = mimeMessage.getSubject();

			fromEmail = ((InternetAddress) mimeMessage.getFrom()[0])
					.getAddress();
			log.debug("Receieved message from " + fromEmail + " subject "
					+ mailSubject);

			// get gody and attachment 
			// from http://jeremyblythe.blogspot.com/2009/12/gae-128-fixes-mail-but-not-jaxb.html
			Object content = mimeMessage.getContent();

			String bodyText = "";
			byte[] imageData = null;
			if (content instanceof String) {
				bodyText = (String) content;
			} else if (content instanceof Multipart) {
				Multipart multipart = (Multipart) content;
				Part part = multipart.getBodyPart(0);
				Object partContent = part.getContent();
				if (partContent instanceof String) {
					bodyText = (String) partContent;
				}
				// extract attached image if any
				imageData = getMailAttachmentBytes(multipart);
			}

			// create item from mail
			SwagItem swagItem = new SwagItem();
			swagItem.setOwner(fromEmail);
			swagItem.setName(mailSubject);
			swagItem.setDescription(bodyText);
			swagItem.setImageBytes(imageData);
			itemService.save(swagItem);
			sendItemAddedSuccessfullyEmail(fromEmail, swagItem);
		} catch (Exception e) {
			log.error("Problem with incoming message from " + fromEmail, e);
			// report error to sender
			sendItemAddExceptionEmail(fromEmail, e);
			// report error to Sam
			sendItemAddExceptionEmailToAdmin(e);
		} finally {
			// this is for if this method is called from TaskQueues (it's not
			// right now)
			// always send status OK or Appengine Task Queues will keep retrying
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}

	/**
	 * From
	 * http://java.sun.com/developer/onlineTraining/JavaMail/contents.html#JavaMailMessage
	 * 
	 * @param attachmentInputStream
	 * @param mimeMultipart
	 * @return image data from attachment or null if there is none
	 * @throws MessagingException
	 * @throws IOException
	 */
	private byte[] getMailAttachmentBytes(Multipart mimeMultipart)
			throws MessagingException, IOException {
		InputStream attachmentInputStream = null;
		try {
			for (int i = 0, n = mimeMultipart.getCount(); i < n; i++) {
				String disposition = mimeMultipart.getBodyPart(i)
						.getDisposition();
				if (disposition == null) {
					continue;
				}
				if ((disposition.equals(Part.ATTACHMENT) || (disposition
						.equals(Part.INLINE)))) {
					attachmentInputStream = mimeMultipart.getBodyPart(i)
							.getInputStream();
					byte[] imageData = getImageDataFromInputStream(attachmentInputStream);
					return imageData;
				}
			}
		} finally {
			try {
				if (attachmentInputStream != null)
					attachmentInputStream.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	private void sendItemAddExceptionEmail(String fromEmail, Exception e) {
		send(
				fromEmail,
				"Your Item email upload failed :(",
				"<b>Please <a href=\"http://code.google.com/p/springstarterapp/issues/entry?template=Defect%20report%20from%20user\">"
						+ "report this issue</a> (requires a google account)</b><br/><br/>Exception:<br/>"
						+ e.toString());
	}

	private void sendItemAddExceptionEmailToAdmin(Exception e) {
		send(ADMIN_EMAIL, "SwagItem email upload failed :(", e.toString());
	}

	private void sendItemAddedSuccessfullyEmail(String fromEmail,
			SwagItem swagItem) {
		send(
				fromEmail,
				"Your item: "
						+ swagItem.getName()
						+ " has been successfuly created.  You may want to fill in additional item information",
				"\n\n<br/><br/>See Your Item here: http://springstarterapp.appspot.com/springmvc/edit/"
						+ swagItem.getKey());
	}

	public void send(String email, String subject, String msgBody) {
		log.debug("Processing send for " + subject + " at " + new Date());
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(ADMIN_EMAIL));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					email));
			msg.setSubject(subject);
			msg.setContent(msgBody, "text/html");
			Transport.send(msg);
			log.debug("sending mail to " + email);
		} catch (Exception e) {
			log.error("sender is " + ADMIN_EMAIL, e);
		}
	}

	public byte[] getImageDataFromInputStream(InputStream inputStream)
			throws LoadImageFromURLException, ImageTooLargeException {
		BufferedInputStream bis = null;
		ByteArrayOutputStream bos = null;
		try {
			bis = new BufferedInputStream(inputStream);
			// write it to a byte[] using a buffer since we don't know the exact
			// image size
			byte[] buffer = new byte[1024];
			bos = new ByteArrayOutputStream();
			int i = 0;
			while (-1 != (i = bis.read(buffer))) {
				bos.write(buffer, 0, i);
			}
			byte[] imageData = bos.toByteArray();
			if (imageData.length > 1000000) {
				throw new ImageTooLargeException("from email", 1000000);
			}
			return imageData;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
}