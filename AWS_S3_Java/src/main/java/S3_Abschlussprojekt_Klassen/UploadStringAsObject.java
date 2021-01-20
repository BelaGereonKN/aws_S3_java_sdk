package S3_Abschlussprojekt_Klassen;

import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;


import java.io.IOException;

public class UploadStringAsObject {

	public static void main(String[] args) throws IOException {
		
		String bucketName = Constants.bucketName;
		String keyName = "Dokument Beispiel";
		String uploadString = "This was Uploaded by Bela Géréon";
		/*
		 * Disables SSL Certificate Check
		 * WARNING: Not recommended for production, don't use if possible
		 */
		System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "True");
		
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
		
		try {
			// This code expects that you have AWS credentials set up per:
			// https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(credentials))
					// Sets up a custom endpoint, delete if not needed
					.withEndpointConfiguration( new AmazonS3ClientBuilder.EndpointConfiguration(
							Constants.endpoint,
							Constants.region)) 
					.build();
				
			// Upload a text string as a new object.
			s3Client.putObject(bucketName, keyName, uploadString);
			System.out.println("The Object was uploaded successfully.");
			
		} catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
        	
        }
	}
}


