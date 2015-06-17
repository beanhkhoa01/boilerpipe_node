### Boilerpipe Node

This is a utility application to interface boilerpipe (a HTML parsing library) and an OTP system.
This application is of acceptable quality, but has not be battle hardened

### License
All code in this library comes under the P2P Production License 
http://p2pfoundation.net/Peer_Production_License

Essentially, you can use it for fun or to learn, or as part of an open source library / system for free, 
but a commerical enterprise should request the ability to use, change or update the code for their commercial purpose. 
This may incur a fee. Please contact us through github to discuss your requirements

### Rationale

We wrote a parsing system in Erlang which *kind* of worked, but required various pieces of intervention that became fiddly to use on a larger scale.
At this point a new strategy was investigated, and boilerpipe seemed an acceptable solution (i.e. parse all the 'text' from HTML).
Combining this with the Language detector library and JInterface was actually quicker that rewriting the functionality in erlang, hence the node

### Dependencies

* Jinterface - OTP integration
* boilerpipe - HTML parsing
* shuyo - Language Detection
* log4j, apache-commons-configuration, slf4j - general stuff

### Build
./build.sh  or mvn package

### Run
./run.sh or java -jar target

### API

Parse: 

`{boilerpipe,boilerpipe@hostname}` ! `{ CallingPid, UrlOrLocalAbsolutePath }`

Response:

`{ok, ParseResult}`

`{error, ErrorType}`

ErrorType:

`{not_found, c}` 

When the file is not accessible (you probably need to use filename:absname(Path) before you call this)

`{invalid_url, {UrlOrLocalAbsolutePath, ErrorDetail } }` 

`{boilerpipe_exception, {UrlOrLocalAbsolutePath, ErrorDetail } }`
    
ErrorDetail: this is exception message from boilerpipe

### Design

We start the OTP node, and an OTP Message box (both names configurable)
We create a thread pool, and each thread gets an unnamed  OTP message box
Assuming a message is sent to the message box on this node as a 
{ ReplyTo,Path } when is_pid(ReplyTo), is_list(Path)

1. A thread is taken from the thread pool and executes ProcessingRunnable.run()
2. We deduce if the Message sent is a local file or a URL
3. We call boilerpipe to parse the file or the URI
4. We apply filters to each line
5. We return the accepted lines to the calling pid via the message box of the processing thread
i.e.
    `ReplyTo ! Result`
    
Result is either 
    `{ok, Text }`
    or
    `{error, Message }`
    
### Configuration

sname=boilerpipe 
setcookie=wordreel
num_processors=10
processing_server_name=boilerpipe
language_detector_profile_dir=/opt/dev/java/github/shuyo/language-detection/profiles
filter_numerical_density=0.1
language=en
language_certainity=0.95
strip_unicode=true
    
### Caveats 

There are no unit tests. This is due to the nature of this application: more or less pure integration. 
It works or it doesn't as part of a set of nodes, and currently it works.

Log4J integration is hackily implemented. 


