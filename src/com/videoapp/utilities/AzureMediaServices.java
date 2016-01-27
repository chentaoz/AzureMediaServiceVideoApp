package com.videoapp.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.EnumSet;


import com.microsoft.windowsazure.services.core.Configuration;
import com.microsoft.windowsazure.services.core.ServiceException;
import com.microsoft.windowsazure.services.media.MediaConfiguration;
import com.microsoft.windowsazure.services.media.MediaContract;
import com.microsoft.windowsazure.services.media.MediaService;
import com.microsoft.windowsazure.services.media.WritableBlobContainerContract;
import com.microsoft.windowsazure.services.media.models.AccessPolicy;
import com.microsoft.windowsazure.services.media.models.AccessPolicyInfo;
import com.microsoft.windowsazure.services.media.models.AccessPolicyPermission;
import com.microsoft.windowsazure.services.media.models.Asset;
import com.microsoft.windowsazure.services.media.models.AssetFile;
import com.microsoft.windowsazure.services.media.models.AssetFileInfo;
import com.microsoft.windowsazure.services.media.models.AssetInfo;
import com.microsoft.windowsazure.services.media.models.AssetOption;
import com.microsoft.windowsazure.services.media.models.ErrorDetail;
import com.microsoft.windowsazure.services.media.models.Job;
import com.microsoft.windowsazure.services.media.models.JobInfo;
import com.microsoft.windowsazure.services.media.models.JobState;
import com.microsoft.windowsazure.services.media.models.ListResult;
import com.microsoft.windowsazure.services.media.models.Locator;
import com.microsoft.windowsazure.services.media.models.LocatorInfo;
import com.microsoft.windowsazure.services.media.models.LocatorType;
import com.microsoft.windowsazure.services.media.models.MediaProcessor;
import com.microsoft.windowsazure.services.media.models.MediaProcessorInfo;
import com.microsoft.windowsazure.services.media.models.Task;
import com.microsoft.windowsazure.services.media.models.TaskInfo;
import com.microsoft.windowsazure.services.media.models.TaskOption;

public class AzureMediaServices {
	Configuration config = MediaConfiguration.configureWithOAuthAuthentication(
		     "https://media.windows.net/API/",
		     "https://wamsprodglobal001acs.accesscontrol.windows.net/v2/OAuth2-13",
		     "simplevideoapp",
		     "vP9oPs1pG/U+hPCqGvKokofkx0YgcuKZLT+wjuKgsX0=",
		     "urn:WindowsAzureMediaServices");
	MediaContract service;
	public AzureMediaServices(){
		service = MediaService.create(config);
	}
	
	
	public static void main(String [] args){
		String filePath="/Users/chentaoz/Desktop/test.mp4";
		AzureMediaServices ams=new AzureMediaServices();
		try {
			AssetInfo inputAsset=ams.uploadFile(filePath);
			AssetInfo preparedAsset=ams.encode2(inputAsset);
			//ams.deliver(preparedAsset);
			String uri=ams.getStreamingOriginLocator(preparedAsset);
			System.out.println(uri);

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public AssetInfo uploadFile(String filePath) throws ServiceException, FileNotFoundException{
		File inputFilePath = new File(filePath);
		String assetName = inputFilePath.getName().substring(0, inputFilePath.getName().lastIndexOf('.'));
		AssetInfo inputAsset = service.create(Asset.create()
		     .setName(assetName)
		     .setOptions(AssetOption.None));
		AccessPolicyInfo writable = service.create(
		     AccessPolicy.create("writable", 10, EnumSet.of(AccessPolicyPermission.WRITE)));
		LocatorInfo assetBlobStorageLocator = service.create(
		     Locator.create(writable.getId(), inputAsset.getId(), LocatorType.SAS));
		WritableBlobContainerContract writer = service.createBlobWriter(assetBlobStorageLocator);
		writer.createBlockBlob(inputFilePath.getName(), new FileInputStream(inputFilePath));
		service.action(AssetFile.createFileInfos(inputAsset.getId()));
		System.out.println("Input asset ID:"+inputAsset.getId());
		return inputAsset;
		
	}
	
	public AssetInfo uploadFile(InputStream fInputStream, String name) throws ServiceException, FileNotFoundException{
		//File inputFilePath = new File(name);
		String assetName = name.substring(0, name.lastIndexOf('.'));
		AssetInfo inputAsset = service.create(Asset.create()
		     .setName(assetName)
		     .setOptions(AssetOption.None));
		AccessPolicyInfo writable = service.create(
		     AccessPolicy.create("writable", 10, EnumSet.of(AccessPolicyPermission.WRITE)));
		LocatorInfo assetBlobStorageLocator = service.create(
		     Locator.create(writable.getId(), inputAsset.getId(), LocatorType.SAS));
		WritableBlobContainerContract writer = service.createBlobWriter(assetBlobStorageLocator);
		writer.createBlockBlob(name, fInputStream);
		service.action(AssetFile.createFileInfos(inputAsset.getId()));
		System.out.println("Input asset ID:"+inputAsset.getId());
		
		
		return inputAsset;
		
	}
	
	
	
	public AssetInfo encode(AssetInfo inputAsset) throws ServiceException{
		AssetInfo assetToEncode = inputAsset;                                                                         
        
		//Preset reference documentation: http://msdn.microsoft.com/en-us/library/windowsazure/jj129582.aspx          
		String encodingPreset = "H264 Single Bitrate Low Quality SD for Android";                                                                
		                                                                                                              
		ListResult < MediaProcessorInfo > processors = service.list(MediaProcessor.list());                     
		MediaProcessorInfo latestWameMediaProcessor = null;                                                           

		System.out.println("Number of processors:"+processors.size());                                                                                                             
		for (MediaProcessorInfo info : processors) {                                                                  
		    if (info.getName().equals("Media Encoder Standard")) {                                               
		        if (latestWameMediaProcessor == null ||                                                               
		                info.getVersion().compareTo(latestWameMediaProcessor.getVersion()) > 0) {                     
		            latestWameMediaProcessor = info;                                                                  
		        }                                                                                                     

		    }                                                                                                         
		}                                                                                                             
		System.out.println(latestWameMediaProcessor.getName());                                                                                                              
		String outputAssetName = assetToEncode.getName() + " as " + encodingPreset;                                   
		                                                                                                              

		JobInfo job = service.create(Job.create()                                                                     

		        .setName("Encoding " + assetToEncode.getName() + " to " + encodingPreset)                             

		        .addInputMediaAsset(assetToEncode.getId())                                                            
		        .addTaskCreator(Task.create(latestWameMediaProcessor.getId(),                                         
		                "< taskBody >" +                                                                        
		                        "< inputAsset >JobInputAsset(0) </inputAsset >" +                         

		                        "< outputAsset assetName='" + outputAssetName + "' > JobOutputAsset(0) </outputAsset>" +  

		                        "</taskBody>")                                                                  
		                .setConfiguration(encodingPreset)                                                             
		                .setOptions(TaskOption.None)                                                                  
		                .setName("Encoding")                                                                          
		        )                                                                                                     

		);                                                                                                            
		                                                                                                              
		while (true) {                                                                                                
		    JobInfo currentJob = service.get(Job.get(job.getId()));                                                   
		    JobState state = currentJob.getState();                                                                   
		    if (state == JobState.Finished || state == JobState.Canceled ||                                           
		            state == JobState.Error) {                                                                        
		        break;                                                                                                
		    }                                                                                                         
		}                                                                                                             
		                                                                                                              
		job = service.get(Job.get(job.getId()));                                                                      

		if (job.getState() == JobState.Error) {                                                                       
		    ListResult<TaskInfo> tasks = service.list(Task.list(job.getTasksLink()));                           
		    for (TaskInfo task : tasks) {                                                                             
		        System.out.println("Task status for " + task.getName());                                              
		        for (ErrorDetail detail : task.getErrorDetails()) {                                                   
		            System.out.println(detail.getMessage());                                                          
		        }                                                                                                     
		    }                                                                                                         
		}                                                                                                             

		ListResult<AssetInfo> outputAssets = service.list(Asset.list(job.getOutputAssetsLink()));               

		AssetInfo preparedAsset = outputAssets.get(0);    
		
		return preparedAsset;
	}
	
	public String deliver(AssetInfo preparedAsset) throws URISyntaxException, ServiceException{
		AssetInfo streamingAsset = preparedAsset;                                                                                                
        
		double minutesForWhichStreamingUrlIsActive = 365 /* days */ * 12 /* hours */ * 60 /* minutes */;
		Date tenMinutesAgo = new Date(
		          new Date().getTime() - 10 /* minutes */ * 60 /* seconds */ * 1000 /* milliseconds */);
		                                                                                                                                         
		AccessPolicyInfo streaming = service.create(AccessPolicy.create(streamingAsset.getName(),                                                
		        minutesForWhichStreamingUrlIsActive,                                                                                             
		        EnumSet.of(AccessPolicyPermission.READ, AccessPolicyPermission.LIST)));                                                          
		                                                                                                                                         
		String streamingUrl = "";                                                                                                                
		                                                                                                                                         
		ListResult<AssetFileInfo> assetFiles = service.list(AssetFile.list(streamingAsset.getAssetFilesLink()));                           
		AssetFileInfo streamingAssetFile = null;
		for (AssetFileInfo file : assetFiles) {                                                                                                  
		    if (file.getName().toLowerCase().endsWith("m3u8-aapl.ism")) {                                                                        
		        streamingAssetFile = file;                                                                                                       
		        break;                                                                                                                           
		    }                                                                                                                                    
		}                                                                                                                                        
		                                                                                                                                         
		if (streamingAssetFile != null) {                                                                                                        
		    LocatorInfo locator = service.create(                                                                                                
		            Locator.create(streaming.getId(), streamingAsset.getId(), LocatorType.OnDemandOrigin));                                      
		    URI hlsUri = new URI(locator.getPath() + streamingAssetFile.getName() + "/manifest(format=m3u8-aapl)"); 
		    streamingUrl = hlsUri.toString();                                                                                                    
		}                                                                                                                                        
		                                                                                                                                         
		if (streamingUrl.isEmpty()) {                                                                                                            
		    streamingAssetFile = null;                                                                                                           
		    for (AssetFileInfo file : assetFiles) {                                                                                              
		        if (file.getName().toLowerCase().endsWith(".ism")) {                                                                             
		            streamingAssetFile = file;                                                                                                   
		            break;                                                                                                                       
		        }                                                                                                                                
		    }                                                                                                                                    
		    if (streamingAssetFile != null) {                                                                                                    
		                                                                                                                                         
		        LocatorInfo locator = service.create(                                                                                            
		                Locator.create(streaming.getId(), streamingAsset.getId(), LocatorType.OnDemandOrigin));                                  
		        URI smoothUri = new URI(locator.getPath() + streamingAssetFile.getName() + "/manifest");
		        streamingUrl = smoothUri.toString();                                                                                             
		    }                                                                                                                                    
		}                                                                                                                                        
		                                                                                                                                         
		if (streamingUrl.isEmpty()) {                                                                                                            
		    streamingAssetFile = null;                                                                                                           
		    for (AssetFileInfo file : assetFiles) {                                                                                              
		        if (file.getName().toLowerCase().endsWith(".mp4")) {                                                                             
		            streamingAssetFile = file;                                                                                                   
		            break;                                                                                                                       
		        }                                                                                                                                
		    }                                                                                                                                    
		    if (streamingAssetFile != null) {                                                                                                    
		                                                                                                                                         
		        LocatorInfo locator = service.create(                                                                                            
		                Locator.create(streaming.getId(), streamingAsset.getId(), LocatorType.SAS));                                             
		        URI mp4Uri = new URI(locator.getPath());                                                                                         
		        mp4Uri = new URI(mp4Uri.getScheme(), mp4Uri.getUserInfo(), mp4Uri.getHost(), mp4Uri.getPort(),                                   
		                mp4Uri.getPath() + "/" + streamingAssetFile.getName(),                                                                   
		                mp4Uri.getQuery(), mp4Uri.getFragment());                                                                                
		        streamingUrl = mp4Uri.toString();                                                                                                
		    }                                                                                                                                    
		}                                                                                                                                        
		                                                                                                                                         
		System.out.println("Streaming Url: " + streamingUrl);  
		
		return streamingUrl;
	}
	
	
	// encode 2
	 public  AssetInfo encode2(AssetInfo assetToEncode)
		        throws ServiceException, InterruptedException {

		        // Retrieve the list of Media Processors that match the name
		        ListResult<MediaProcessorInfo> mediaProcessors = service
		                        .list(MediaProcessor.list().set("$filter", String.format("Name eq '%s'", "Media Encoder Standard")));
		        //ListResult < MediaProcessorInfo > processors = service.list(MediaProcessor.list());  
		        // Use the latest version of the Media Processor
		        MediaProcessorInfo mediaProcessor = null;
		        for (MediaProcessorInfo info : mediaProcessors) {
		            if (null == mediaProcessor || info.getVersion().compareTo(mediaProcessor.getVersion()) > 0) {
		                mediaProcessor = info;
		            }
		        }

		        System.out.println("Using Media Processor: " + mediaProcessor.getName() + " " + mediaProcessor.getVersion());

		        // Create a task with the specified Media Processor
		        String outputAssetName = String.format("%s as %s", assetToEncode.getName(), "H264 Single Bitrate Low Quality SD for Android");
		        String taskXml = "<taskBody><inputAsset>JobInputAsset(0)</inputAsset>"
		                + "<outputAsset assetCreationOptions=\"0\"" // AssetCreationOptions.None
		                + " assetName=\"" + outputAssetName + "\">JobOutputAsset(0)</outputAsset></taskBody>";

		        Task.CreateBatchOperation task = Task.create(mediaProcessor.getId(), taskXml)
		                .setConfiguration("H264 Single Bitrate Low Quality SD for Android").setName("Encoding");

		        // Create the Job; this automatically schedules and runs it.
		        Job.Creator jobCreator = Job.create()
		                .setName(String.format("Encoding %s to %s", assetToEncode.getName(), "H264 Single Bitrate Low Quality SD for Android"))
		                .addInputMediaAsset(assetToEncode.getId()).setPriority(2).addTaskCreator(task);
		        JobInfo job = service.create(jobCreator);

		        String jobId = job.getId();
		        System.out.println("Created Job with Id: " + jobId);

		        // Check to see if the Job has completed
		        checkJobStatus(jobId);
		        // Done with the Job

		        // Retrieve the output Asset
		        ListResult<AssetInfo> outputAssets = service.list(Asset.list(job.getOutputAssetsLink()));
		        return outputAssets.get(0);
		    }
	   public String getStreamingOriginLocator(AssetInfo asset) throws ServiceException {
	        // Get the .ISM AssetFile
	        ListResult<AssetFileInfo> assetFiles = service.list(AssetFile.list(asset.getAssetFilesLink()));
	        AssetFileInfo streamingAssetFile = null;
	        for (AssetFileInfo file : assetFiles) {
	            if (file.getName().toLowerCase().endsWith(".ism")) {
	                streamingAssetFile = file;
	                break;
	            }
	        }

	        AccessPolicyInfo originAccessPolicy;
	        LocatorInfo originLocator = null;

	        // Create a 30-day readonly AccessPolicy
	        double durationInMinutes = 60 * 24 * 30;
	        originAccessPolicy = service.create(
	                AccessPolicy.create("Streaming policy", durationInMinutes, EnumSet.of(AccessPolicyPermission.READ)));

	        // Create a Locator using the AccessPolicy and Asset
	        originLocator = service
	                .create(Locator.create(originAccessPolicy.getId(), asset.getId(), LocatorType.OnDemandOrigin));

	        // Create a Smooth Streaming base URL
	        return originLocator.getPath() + streamingAssetFile.getName() + "/manifest";
	    }
	   
	    private  void checkJobStatus(String jobId) throws InterruptedException, ServiceException {
	        boolean done = false;
	        JobState jobState = null;
	        while (!done) {
	            // Sleep for 5 seconds
	            Thread.sleep(5000);

	            // Query the updated Job state
	            jobState = service.get(Job.get(jobId)).getState();
	            System.out.println("Job state: " + jobState);

	            if (jobState == JobState.Finished || jobState == JobState.Canceled || jobState == JobState.Error) {
	                done = true;
	            }
	        }
	    }
	
}
