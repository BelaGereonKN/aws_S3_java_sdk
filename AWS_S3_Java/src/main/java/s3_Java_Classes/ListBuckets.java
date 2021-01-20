package s3_Java_Classes;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;


import java.io.IOException;
import java.util.List;

public class ListBuckets {

		public static void main(String[] args) throws IOException {
				
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
			
				
				List<Bucket> buckets = s3Client.listBuckets();
				System.out.println("Your Amazon S3 buckets are:");
				for (Bucket b : buckets) {
				    System.out.println("* " + b.getName());
				}
		
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

