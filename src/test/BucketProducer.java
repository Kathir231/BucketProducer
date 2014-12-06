package test;

import java.io.InputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class BucketProducer {

	// S3 object instance
	private AmazonS3 s3ObjectInstance = null;

	/**
	 * Constructor initialises the BucketProduer class and connects with the account 
	 * using the credentials provided in the properties file
	 * Default Region: Regions.US_WEST_2
	 */
	public BucketProducer(){
		System.out.println("Initialising S3 Bucket\n");
		s3ObjectInstance = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
		s3ObjectInstance.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

	/**
	 * Loads the provided file name in the s3 bucket
	 * @param bucket - Bucket name
	 * @param filename - File name to be saved in the s3 storage
	 * @param inStream - File input stream
	 * @param metadata - File meta data
	 * @return boolean - True : Upload was successful
	 * 					 False: Upload Failed
	 */
	public boolean PutObjectRequest(String bucket, String filename, InputStream inStream, ObjectMetadata metadata) {
		boolean isSucessful = false;
		try {
			PutObjectResult result = s3ObjectInstance.putObject(new PutObjectRequest(bucket, filename, inStream, metadata));
			if(null != result) isSucessful = true;
		} catch (AmazonServiceException e) {
			e.printStackTrace();
		} catch (AmazonClientException e) {
			e.printStackTrace();
		}
		return isSucessful;
	}
}
