#!/bin/bash
find ./target -name boilerpipe_node*.jar | xargs java -Dlog4j.configuration=file:./log4j.properties $@ -jar  