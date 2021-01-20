package s3_Java_Classes;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.File;
import java.io.IOException;

public class UploadObject {

	    public static void main(String[] args) throws IOException, AmazonClientException, InterruptedException {
	        
	        String bucketName = Constants.bucketName;
	        String keyName = "Upload with new path test";
	        String objectName = "Dokument Beispiel.txt";
	        
	        String filePath = Constants.filePath + "\\" + objectName;

	      //This part gets your AWS credentials from the default location
			AWSCredentials credentials = null;
			try {
				credentials = new ProfileCredentialsProvider("default").getCredentials();
			} catch (Exception e) {
				throw new AmazonClientException(
		                   "Cannot load the credentials from the credential profiles file. " +
		                   "Please make sure that your credentials file is at the correct " +
		                   "location and is in valid format.",
		                   e);
			}
			
			/*
			 * Disables SSL Certificate Check
			 * WARNING: Not recommended for production, don't use if possible
			 */
			System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "True");
	        
	        try {
	        	// This code expects that you have AWS credentials set up per:
				// https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
				AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
						.withCredentials(new AWSStaticCredentialsProvider(credentials))
						.withEndpointConfiguration( new AmazonS3ClientBuilder.EndpointConfiguration(
								Constants.endpoint,
								Constants.region)) // Sets up a custom endpoint, delete if not needed
						.build();
				
	            TransferManager tm = TransferManagerBuilder.standard()
	                    .withS3Client(s3Client)
	                    .build();

	            // TransferManager processes all transfers asynchronously,
	            // so this call returns immediately.
	            Upload upload = tm.upload(bucketName, keyName, new File(filePath));
	            System.out.println("Object upload started");

	            //Optionally, wait for the upload to finish before continuing.
	            upload.waitForCompletion();
	            System.out.println("Object upload complete");
	        } catch (AmazonServiceException e) {
	            // The call was transmitted successfully, but Amazon S3 couldn't process 
	            // it, so it returned an error response.
	            e.printStackTrace();
	        } catch (SdkClientException e) {
	            // Amazon S3 couldn't be contacted for a response, or the client
	            // couldn't parse the response from Amazon S3.
	            e.printStackTrace();
	        }
	    }
	}
	

