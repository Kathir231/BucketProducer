package test;

//Import required java libraries
import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.amazonaws.services.s3.model.ObjectMetadata;

public class FileUpload extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private boolean isMultipart;
	private int maxFileSize = 50 * 1024;
	private int maxMemSize = 40 * 1024;
	private static final String bucket = "eciiiassignment";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		// Check that we have a file upload request
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");
		java.io.PrintWriter out = response.getWriter();
		if (!isMultipart) {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<p>No file uploaded</p>");
			out.println("</body>");
			out.println("</html>");
			return;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("c:\\temp"));
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);
		//boolean to check whether upload was successful
		boolean isUploadSuccessful = false;
		try {
			// Initialising the bucket producer
			BucketProducer bucketProducer = new BucketProducer();
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);
			// Process the uploaded file items
			Iterator i = fileItems.iterator();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {
					// Get the uploaded file parameters
					String fileName = fi.getName();
					long sizeInBytes = fi.getSize();
					// Write the file
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentLength(sizeInBytes);
					isUploadSuccessful = bucketProducer.PutObjectRequest(bucket, fileName, fi.getInputStream(), metadata);
				}
			}
		} catch (Exception ex) {
			isUploadSuccessful = false;
			System.out.println(ex);
		}
		if(isUploadSuccessful){
			out.println("<h1> Uploaded Successfully </h1>");
		}else{
			out.println("<h1> File Upload Failed </h1>");
		}
		out.println("</body>");
		out.println("</html>");
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		throw new ServletException("GET method used with "
				+ getClass().getName() + ": POST method required.");
	}
}