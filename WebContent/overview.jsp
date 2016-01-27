<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.util.ArrayList,com.videoapp.utilities.VideoDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Welcome! Video App</title>

    <!-- Bootstrap Core CSS -->
    <link href="resources/theme/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS 1-->
    <link href="resources/theme/css/3-col-portfolio.css" rel="stylesheet">
    
    <!-- Custom CSS 2-->
    <link href="resources/dropzone/dropzone.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style>
	    .playpause {
		    background-image:url(http://png-4.findicons.com/files/icons/2315/default_icon/256/media_play_pause_resume.png);
		    background-repeat:no-repeat;
		    width:50%;
		    height:50%;
		    position:absolute;
		    left:0%;
		    right:0%;
		    top:0%;
		    bottom:0%;
		    margin:auto;
		    background-size:contain;
		    background-position: center;
		}
		.tag {
		  background: #eee;
		  border-radius: 3px 0 0 3px;
		  color: #999;
		  display: inline-block;
		  height: 26px;
		  line-height: 26px;
		  padding: 0 20px 0 23px;
		  position: relative;
		  margin: 0 10px 10px 0;
		  text-decoration: none;
		  -webkit-transition: color 0.2s;
		}
		.tag::before {
  background: #fff;
  border-radius: 10px;
  box-shadow: inset 0 1px rgba(0, 0, 0, 0.25);
  content: '';
  height: 6px;
  left: 10px;
  position: absolute;
  width: 6px;
  top: 10px;
}

.tag::after {
  background: #fff;
  border-bottom: 13px solid transparent;
  border-left: 10px solid #eee;
  border-top: 13px solid transparent;
  content: '';
  position: absolute;
  right: 0;
  top: 0;
}

.tag:hover {
  background-color: crimson;
  color: white;
}

.tag:hover::after {
   border-left-color: crimson; 
}
	
    </style>
</head>
<body>
<!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">VideoApp</a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li>
                        <a data-toggle="modal" data-target="#uploadPlate">Upload</a>
                    </li>  
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

    <!-- Page Content -->
    <div class="container">

        <!-- Page Header -->
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">All Videos
                    <small>Previews</small>
                    <%ArrayList<VideoDAO> vs=(ArrayList<VideoDAO>)(request.getAttribute("videos")); %>
                    <small style="float:right">Totally <strong><%= vs==null?0:vs.size() %></strong> Videos</small>
                </h1>
            </div>
        </div>
        <!-- /.row -->

        <!-- Projects Row -->
        <div class="row">
        <% 
        if(vs!=null)   		
        for(VideoDAO v : vs){ %>
            <div class="col-md-4 portfolio-item video_container" >
            	 <a href="<%=v.getUri()%>" class="btn btn-default player-btn" style="position:absolute;top:-5px">Full Window Play</a> 
				<small style="position:absolute;top:0px;right:65px"><%=v.getTime()%></small>
                <a href="#">
                    <video width="320" height="240"  class="embed-responsive-item video" id="video_<%=v.getId()%>">
					  <source src="<%=v.getUri()%>" type="video/mp4">
					  Your browser does not support HTML5 video.
					</video>
					<div class="playpause"></div>					
                </a>
                <div style="position:absolute;bottom:-10px" >
                     <a href="#" class="tag"><%=v.getTag() %> </a>                 
                </div>  
                 <div class=" tag-input " style="position:absolute;bottom:-10px; "> 
					  <input class="form-control tag-input-input"  type="text" style="width:50% ;display: inline-block">  
					 
					  <a class='btn btn-default' style="display: inline-block" onclick="submitTag(this,<%=v.getId()%>)">Target it</a>					 
				</div>            
            </div>
         <%} %>          
        </div>
       
        <hr>


        <!-- Footer -->
        <footer>
            <div class="row">
                <div class="col-lg-12">
                    <p>Copyright &copy; VideoApp 2016</p>
                </div>
            </div>
            <!-- /.row -->
        </footer>

    </div>
    <div class="modal fade" id="uploadPlate" role="dialog">
	    <div class="modal-dialog modal-sm">
	      <div class="modal-content">
	        <div class="modal-header">
	          <button type="button" class="close" data-dismiss="modal">&times;</button>
	          <h4 class="modal-title">UPLOAD VIDEO</h4>
	        </div>
	        <div class="modal-body">
	        
	          <div class="box" id="dropzone">
				  <form action="/VideoApp/welcome" method="post" class="dropzone" id="my-awesome-dropzone">
						<div class="table table-striped files dropzone-previews dropzoner" id="previews"> 
							<div class="dz-preview dz-file-preview" id="template">  
								<div class="dz-image"><img data-dz-thumbnail /></div>  
								<div class="dz-details">    
									<div class="dz-size"><span data-dz-size></span></div>    
									<div class="dz-filename">
										<span data-dz-name></span><br/>
										<span> <a class="settag" onclick="setTag(this)" data-dz-tag>Tag the video</a></span>
									</div>
									
								</div>  
								<div class="dz-progress"><span class="dz-upload" data-dz-uploadprogress></span></div>  
								<div class="dz-error-message"><span data-dz-errormessage></span></div>  
								<div class="dz-success-mark">    
									<svg width="54px" height="54px" viewBox="0 0 54 54" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:sketch="http://www.bohemiancoding.com/sketch/ns">     
										<title>Check</title>      
										<defs></defs>      
										<g id="Page-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd" sketch:type="MSPage">        
											<path d="M23.5,31.8431458 L17.5852419,25.9283877 C16.0248253,24.3679711 13.4910294,24.366835 11.9289322,25.9289322 C10.3700136,27.4878508 10.3665912,30.0234455 11.9283877,31.5852419 L20.4147581,40.0716123 C20.5133999,40.1702541 20.6159315,40.2626649 20.7218615,40.3488435 C22.2835669,41.8725651 24.794234,41.8626202 26.3461564,40.3106978 L43.3106978,23.3461564 C44.8771021,21.7797521 44.8758057,19.2483887 43.3137085,17.6862915 C41.7547899,16.1273729 39.2176035,16.1255422 37.6538436,17.6893022 L23.5,31.8431458 Z M27,53 C41.3594035,53 53,41.3594035 53,27 C53,12.6405965 41.3594035,1 27,1 C12.6405965,1 1,12.6405965 1,27 C1,41.3594035 12.6405965,53 27,53 Z" id="Oval-2" stroke-opacity="0.198794158" stroke="#747474" fill-opacity="0.816519475" fill="#FFFFFF" sketch:type="MSShapeGroup"></path>      
										</g>    
									</svg>  
								</div>  
								<div class="dz-error-mark">   
									<svg width="54px" height="54px" viewBox="0 0 54 54" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:sketch="http://www.bohemiancoding.com/sketch/ns">      
										<title>Error</title>      
										<defs></defs>      
										<g id="Page-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd" sketch:type="MSPage">        
											<g id="Check-+-Oval-2" sketch:type="MSLayerGroup" stroke="#747474" stroke-opacity="0.198794158" fill="#FFFFFF" fill-opacity="0.816519475">          
												<path d="M32.6568542,29 L38.3106978,23.3461564 C39.8771021,21.7797521 39.8758057,19.2483887 38.3137085,17.6862915 C36.7547899,16.1273729 34.2176035,16.1255422 32.6538436,17.6893022 L27,23.3431458 L21.3461564,17.6893022 C19.7823965,16.1255422 17.2452101,16.1273729 15.6862915,17.6862915 C14.1241943,19.2483887 14.1228979,21.7797521 15.6893022,23.3461564 L21.3431458,29 L15.6893022,34.6538436 C14.1228979,36.2202479 14.1241943,38.7516113 15.6862915,40.3137085 C17.2452101,41.8726271 19.7823965,41.8744578 21.3461564,40.3106978 L27,34.6568542 L32.6538436,40.3106978 C34.2176035,41.8744578 36.7547899,41.8726271 38.3137085,40.3137085 C39.8758057,38.7516113 39.8771021,36.2202479 38.3106978,34.6538436 L32.6568542,29 Z M27,53 C41.3594035,53 53,41.3594035 53,27 C53,12.6405965 41.3594035,1 27,1 C12.6405965,1 1,12.6405965 1,27 C1,41.3594035 12.6405965,53 27,53 Z" id="Oval-2" sketch:type="MSShapeGroup"></path>        
											</g>     
										</g>    
									</svg>  
								</div>
							</div>							
													
													
						</div>
				  </form>
	          </div>
	          	<div class="textinput" id="tagInputDiv">
									<div class="col-lg-12">
										    <div class="input-group">
										      <input type="text" class="form-control" placeholder="music/news/sports..." id="tagInput">
										      <span class="input-group-btn">
										        <button class="btn btn-secondary" type="button" onclick="inputSetTag()")>Tag it!</button>
										      </span>
										    </div>
  									</div>
				</div>
	        </div>
	        <div class="modal-footer">
	          <button type="button" class="btn btn-default" id="submit">Upload</button>
	        </div>
	      </div>
	    </div>
  	</div>
    <!-- /.container -->
    
    

    <!-- jQuery -->
    <script src="resources/theme/js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="resources/theme/js/bootstrap.min.js"></script>
    
    <script src="resources/dropzone/dropzone.js"></script>
    <script>
    function makeid()
    {
        var text = "";
        var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for( var i=0; i < 5; i++ )
            text += possible.charAt(Math.floor(Math.random() * possible.length));

        return text;
    }
   	var previewNode = document.querySelector("#template");
	previewNode.id = "";
	var previewTem = previewNode.parentNode.innerHTML;
	previewNode.parentNode.removeChild(previewNode);
   	var accept = "video/*";
	var myDropzone;
    $(function(){
    	  $("#tagInputDiv").hide();
    	
    	  $("#submit").prop("disabled",true);
    	  Dropzone.options.myAwesomeDropzone = {
    	    maxFilesize: 1000,
    	    addRemoveLinks: true,
    	    dictResponseError: 'Server not Configured',
    	    acceptedFiles: accept,
    	    autoProcessQueue:false,
    	    previewTemplate: previewTem,
    	    previewsContainer: "#previews",
    	  
    	    init:function(){
    	      myDropzone=this;
    	      var self = this;
    	      // config
    	      self.options.addRemoveLinks = true;
    	      self.options.dictRemoveFile = "Delete";
    	      //New file added
    	      self.on("addedfile", function (file) {
    	        console.log('new file added ', file);
    	        $("#submit").prop("disabled",false); 
    	    	$('#submit').attr('class', "btn btn-success");
    	    	file.tag="default";
    	    	var id=makeid();
    	    	file.nodeId=id;
    	    	var tagNode=file.previewElement.querySelectorAll("[data-dz-tag]");
    	    	$(tagNode).attr("id",id);
    	    	
    	    
    	        
    	      });
    	      // Send file starts
    	      self.on("sending", function (file,xhr, formData) {
    	        console.log('upload started', file);
    	        $('.meter').show();
    	        formData.append("tag", file.tag);
    	      });
    	      
    	      // File upload Progress
    	      self.on("totaluploadprogress", function (progress) {
    	        console.log("progress ", progress);
    	        $('.roller').width(progress + '%');
    	      });

    	      self.on("queuecomplete", function (progress) {
    	        $('.meter').delay(999).slideUp(999);
    	      });
    	      
    	      // On removing file
    	      self.on("removedfile", function (file) {
    	        console.log(file);
    	      });
    	    }
    	  };
    	});
    $("#testb").click(function(){
    	console.log(myDropzone.getAcceptedFiles());
    	console.log(myDropzone.getRejectedFiles());
    	console.log(myDropzone.getQueuedFiles());
    	console.log(myDropzone.getUploadingFiles());
    	//console.log(myDropzone.options.previewTemplate);
    });
    $("#submit").click(function(){
    	myDropzone.processQueue();
    	$("#submit").prop("disabled",true);
    	$('#submit').attr('class', "btn btn-default");
    });
    var currentFile;
    function setTag(node){
    	 var parent=node.parentNode;
    	 var id=$(node).attr("id");
    	 var files=myDropzone.getQueuedFiles();
    	
    	 for(var i=0; i<files.length; i++){
    		 if(files[i].nodeId==id)
    			 currentFile=files[i]; 
    		 console.log(files[i]);
    	 }
    	 
    	console.log(currentFile);
    	$("#tagInputDiv").show();
    	$("#tagInput").focus();
    	
     }
    
    function inputSetTag(){
    	var tag=$("#tagInput").val();
    	currentFile.tag=tag;
    	$("#tagInput").val("");
    	$("#tagInputDiv").hide();
    	$("#"+currentFile.nodeId).html(tag);
    }
    
    $('.playpause').parent().click(function () {
        if($(this).children(".video").get(0).paused){
            $(this).children(".video").get(0).play();
            $(this).children(".playpause").fadeOut();
        }else{
           $(this).children(".video").get(0).pause();
            $(this).children(".playpause").fadeIn();
        }
    });  
     

    $(".player-btn").fadeOut();
    $(".video_container").mouseenter(function(){
    	$(this).children(".player-btn").fadeIn();
    });
    $(".video_container").mouseleave(function(){
    	$(this).children(".player-btn").fadeOut();
    });
    $(".tag-input").hide();
    $(".tag").parent().click(function(){
    	var theNode=this;
    	var tagContent=$(this).text().trim();
    	var tag_input=$(theNode).parent().children(".tag-input").get(0);
    	$($(tag_input).children(".tag-input-input").get(0)).val(tagContent);
    	
		$(this).children(".tag").fadeOut(function(){
				$(theNode).parent().children(".tag-input").show();
				$($(theNode).parent().children(".tag-input").get(0)).children(".tag-input-input").focus();
			});
		
		
	});
    
    $('html').click(function(e) {                    
    	   if(!$(e.target).hasClass('tag-input') )
    	   {
    		
	   			$(".tag-input").each(function(){
	   				$(this).hide();
	   				$($(this).parent().children("div").get(0)).children(".tag").fadeIn();
	   			});
    	   }
    	}); 
    
    function submitTag(node,id){
    	var tagContent=$($(node).parent().children().get(0)).val();
    	$.ajax({
    		url:"ajaxService",
    		type:"post",
    		data:"sev=tagSev&id="+id+"&tag="+tagContent,
    		success:function(){
    			$($(node).parent().parent().children("div").get(0)).children(".tag").text(tagContent);
    			$(node).parent().hide();
    			$($(node).parent().parent().children("div").get(0)).children(".tag").fadeIn();
    			
    		}
    	})
    }
    
    </script>

</body>
</html>