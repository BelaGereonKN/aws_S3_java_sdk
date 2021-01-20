package S3_Abschlussprojekt_Klassen;

public class Constants {
	
	// custom endpoint, needed to connect to servers not associated with amazon
	public static final String endpoint = "*** ENTER YOUR CUSTOM ENDPOINT URL ***";
	
	// region your bucket is stored in, best used if only one 
	// or multiple buckets within the same region are accessed
	public static final String region = "us-east-1";
	
	// name of the bucket, best used when only one bucket is accessed regularly
	public static final String bucketName = "*** ENTER YOUR BUCKET NAME ***";
	
	// path from where you want to upload object to S3 or download objects to from S3
	public static final String filePath = "*** ENTER YOUR DEFAULT FILE PATH ***";
	
}
