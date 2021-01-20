package s3_Java_Classes;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;

import java.io.IOException;

public class BucketVersionConfig {
	
	 /*	A bucket's versioning configuration can be in one of three possible states:
	  *		- Off
	  *		- Enabled
	  *		- Suspended
	  * By default, new buckets are in the off state. 
	  * Once versioning is enabled for a bucket the status can never be reverted to off.
	  */
	
	// Change the status according to the config you want to apply
	public static String status = "Enabled";
	
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
			
			// 1. Enable versioning on the bucket.
        	BucketVersioningConfiguration configuration = 
        			new BucketVersioningConfiguration().withStatus(status);
            
			SetBucketVersioningConfigurationRequest setBucketVersioningConfigurationRequest = 
					new SetBucketVersioningConfigurationRequest(Constants.bucketName,configuration);
			
			s3Client.setBucketVersioningConfiguration(setBucketVersioningConfigurationRequest);
			
			// 2. Get bucket versioning configuration information.
			BucketVersioningConfiguration conf = s3Client.getBucketVersioningConfiguration(Constants.bucketName);
			 System.out.println("bucket versioning configuration status:    " + conf.getStatus());
	
			} catch (AmazonS3Exception amazonS3Exception) {
	            System.out.format("An Amazon S3 error occurred. Exception: %s", amazonS3Exception.toString());
	        } catch (Exception ex) {
	            System.out.format("Exception: %s", ex.toString());
	        }        
	    }
	}
