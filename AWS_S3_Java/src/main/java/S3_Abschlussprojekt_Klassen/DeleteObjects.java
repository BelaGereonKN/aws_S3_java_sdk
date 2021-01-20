package S3_Abschlussprojekt_Klassen;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;

import java.io.IOException;

public class DeleteObjects {

	public static void main(String[] args) throws IOException {
		
		// set this to true if you want to delete multiple objects
		boolean delMultOb = false;
		
		String bucketName = Constants.bucketName;
		// Specify the object that you want to delete here
		String keyName = "Test Object"; // if you only want to delete one file, use this variable
		
		String keyName1 = "Testfile1.txt";
		String keyName2 = "erster test";
		String keyName3 = "zweiter test";
		// etc...
		
		
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
			
			if (delMultOb) {
				DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(Constants.bucketName)
						.withKeys(keyName,
								keyName1,
								keyName2,
								keyName3)
						.withQuiet(false);

				// Verify that the objects were deleted successfully.
				DeleteObjectsResult delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest);
				int successfulDeletes = delObjRes.getDeletedObjects().size();
				System.out.println(successfulDeletes + " objects successfully deleted.");
			} else {
				// Creates a delete object request with the parameters you specified id the object exists
				if (s3Client.doesObjectExist(bucketName, keyName)){	
					s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));					
					System.out.println("The Object has been deleted successfully.");
				} else {
					System.out.println("The Object you are looking for does not exist.\n" + 									"Please make sure that the name you provided is correct and try again.");
				}		
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
	
