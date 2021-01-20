package s3_Java_Classes;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;

import java.io.IOException;
import java.util.List;

public class ListObjects {
	
	// true = show all versions of the objects in your bucket
	// false = show every objects current version
	static boolean showVersions = false;
	
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
			
			if (showVersions) {
				// Retrieve the list of versions. If the bucket contains more versions
	            // than the specified maximum number of results, Amazon S3 returns
	            // one page of results per request.
	            ListVersionsRequest request = new ListVersionsRequest()
	                    .withBucketName(Constants.bucketName)
	                    .withMaxResults(2);
	            VersionListing versionListing = s3Client.listVersions(request);
	            int numVersions = 0, numPages = 0;
	            while (true) {
	                numPages++;
	                for (S3VersionSummary objectSummary :
	                        versionListing.getVersionSummaries()) {
	                    System.out.printf("Retrieved object %s, version %s\n",
	                            objectSummary.getKey(),
	                            objectSummary.getVersionId());
	                    numVersions++;
	                }
	                // Check whether there are more pages of versions to retrieve. If
	                // there are, retrieve them. Otherwise, exit the loop.
	                if (versionListing.isTruncated()) {
	                    versionListing = s3Client.listNextBatchOfVersions(versionListing);
	                } else {
	                    break;
	                }
	            }
	            System.out.println(numVersions + " object versions retrieved in " + numPages + " pages");
			} else {
				// Creates a list with all the objects in your bucket and prints it to the console
				System.out.format("Objects in S3 bucket %s:\n", Constants.bucketName);
				ListObjectsV2Result result = s3Client.listObjectsV2(Constants.bucketName);
				List<S3ObjectSummary> objects = result.getObjectSummaries();
				for (S3ObjectSummary os : objects) {
			    	System.out.println("* " + os.getKey());
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
