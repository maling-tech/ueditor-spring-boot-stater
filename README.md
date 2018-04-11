# ueditor-spring-boot-stater
spring boot starer for ueditor
#### pom.xml 添加依赖
```xml
 <dependency>
    <groupId>com.ml</groupId>
    <artifactId>ueditor-spring-boot-stater</artifactId>
    <version>1.0.0.RELEASE</version>
</dependency>
```
#### 在resources下添加ueditor.config.json
```json
{
  "imageActionName": "uploadimage",
  "imageFieldName": "upfile",
  "imageMaxSize": 2048000,
  "imageAllowFiles": [
    ".png",
    ".jpg",
    ".jpeg",
    ".gif",
    ".bmp"
  ],
  "imageCompressEnable": true,
  "imageCompressBorder": 1600,
  "imageInsertAlign": "none",
  "imageUrlPrefix": "",
  "imagePathFormat": "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}",
  "scrawlActionName": "uploadscrawl",
  "scrawlFieldName": "upfile",
  "scrawlPathFormat": "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}",
  "scrawlMaxSize": 2048000,
  "scrawlUrlPrefix": "",
  "scrawlInsertAlign": "none",
  "snapscreenActionName": "uploadimage",
  "snapscreenPathFormat": "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}",
  "snapscreenUrlPrefix": "",
  "snapscreenInsertAlign": "none",
  "catcherLocalDomain": [
    "127.0.0.1",
    "localhost",
    "img.baidu.com"
  ],
  "catcherActionName": "catchimage",
  "catcherFieldName": "source",
  "catcherPathFormat": "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}",
  "catcherUrlPrefix": "",
  "catcherMaxSize": 2048000,
  "catcherAllowFiles": [
    ".png",
    ".jpg",
    ".jpeg",
    ".gif",
    ".bmp"
  ],
  "videoActionName": "uploadvideo",
  "videoFieldName": "upfile",
  "videoPathFormat": "/ueditor/jsp/upload/video/{yyyy}{mm}{dd}/{time}{rand:6}",
  "videoUrlPrefix": "",
  "videoMaxSize": 102400000,
  "videoAllowFiles": [
    ".flv",
    ".swf",
    ".mkv",
    ".avi",
    ".rm",
    ".rmvb",
    ".mpeg",
    ".mpg",
    ".ogg",
    ".ogv",
    ".mov",
    ".wmv",
    ".mp4",
    ".webm",
    ".mp3",
    ".wav",
    ".mid"
  ],
  "fileActionName": "uploadfile",
  "fileFieldName": "upfile",
  "filePathFormat": "/ueditor/jsp/upload/file/{yyyy}{mm}{dd}/{time}{rand:6}",
  "fileUrlPrefix": "",
  "fileMaxSize": 51200000,
  "fileAllowFiles": [
    ".png",
    ".jpg",
    ".jpeg",
    ".gif",
    ".bmp",
    ".flv",
    ".swf",
    ".mkv",
    ".avi",
    ".rm",
    ".rmvb",
    ".mpeg",
    ".mpg",
    ".ogg",
    ".ogv",
    ".mov",
    ".wmv",
    ".mp4",
    ".webm",
    ".mp3",
    ".wav",
    ".mid",
    ".rar",
    ".zip",
    ".tar",
    ".gz",
    ".7z",
    ".bz2",
    ".cab",
    ".iso",
    ".doc",
    ".docx",
    ".xls",
    ".xlsx",
    ".ppt",
    ".pptx",
    ".pdf",
    ".txt",
    ".md",
    ".xml"
  ],
  "imageManagerActionName": "listimage",
  "imageManagerListPath": "/ueditor/jsp/upload/image/",
  "imageManagerListSize": 20,
  "imageManagerUrlPrefix": "",
  "imageManagerInsertAlign": "none",
  "imageManagerAllowFiles": [
    ".png",
    ".jpg",
    ".jpeg",
    ".gif",
    ".bmp"
  ],
  "fileManagerActionName": "listfile",
  "fileManagerListPath": "/ueditor/jsp/upload/file/",
  "fileManagerUrlPrefix": "",
  "fileManagerListSize": 20,
  "fileManagerAllowFiles": [
    ".png",
    ".jpg",
    ".jpeg",
    ".gif",
    ".bmp",
    ".flv",
    ".swf",
    ".mkv",
    ".avi",
    ".rm",
    ".rmvb",
    ".mpeg",
    ".mpg",
    ".ogg",
    ".ogv",
    ".mov",
    ".wmv",
    ".mp4",
    ".webm",
    ".mp3",
    ".wav",
    ".mid",
    ".rar",
    ".zip",
    ".tar",
    ".gz",
    ".7z",
    ".bz2",
    ".cab",
    ".iso",
    ".doc",
    ".docx",
    ".xls",
    ".xlsx",
    ".ppt",
    ".pptx",
    ".pdf",
    ".txt",
    ".md",
    ".xml"
  ]
}
```
#### 启用Ueditor
```java
@SpringBootApplication
@EnableUeditor
public class UeditorDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UeditorDemoApplication.class, args);
    }
}
```

####
```java
@Controller
public class UeditorController {

    @Autowired
    private UeditorService ueditorService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/ueditor")
    @ResponseBody
    public String ueditor(HttpServletRequest request) {
        return ueditorService.exec(request);
    }
}
```

## 指定保存文件路径
1. 添加controller
```java
@Controller
public class UeditorController {

    @Autowired
    private UeditorService ueditorService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/ueditor")
    @ResponseBody
    public String ueditor(HttpServletRequest request) {
        return ueditorService.exec(request,"D://cache"); //指定文件保存路径
    }
}
```
2. 修改ueditor.config.json
将其中的xxxUrlPrefix值设为 "/userfiles"

3. 设置tomcat虚拟路径，将 "/userfiles" 指向文件保存的路径

