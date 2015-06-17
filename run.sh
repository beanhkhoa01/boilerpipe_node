#!/bin/bash
find ./bin -name boilerpipe_node*.jar | xargs java -DOtpConnection.trace=4 -jar  