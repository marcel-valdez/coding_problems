OUTPUT_DIR=$1
CLASSPATH=$2
TEST_NAME=$3

# compile all source files, appends a : to the end
JAR_CLASSPATH=$(find "$CLASSPATH" -iregex .*\.jar | tr '\n' ' ' |  sed 's/[ ][ ]*/:/g')
# run test
if [ $? -eq 0 ]; then
  java -classpath "$JAR_CLASSPATH$OUTPUT_DIR" RunJunit "$OUTPUT_DIR" $TEST_NAME
fi

exit $?
