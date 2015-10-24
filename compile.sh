SRC_DIR=$1
TEST_DIR=$2
OUTPUT_DIR=$3
CLASSPATH=$4

__compile() {
  if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir "$OUTPUT_DIR"
  fi

  javac -classpath "$JAR_CLASSPATH:$OUTPUT_DIR" -d "$OUTPUT_DIR" $(find "$1" -iregex .*\.java)
}

# gather libararies
JAR_CLASSPATH=$(find "$CLASSPATH" -iregex .*\.jar | tr '\n' ' ' |  sed 's/[ ][ ]*/:/g')
# compile all source files
if [ $? -eq 0 ]; then
  # compile all test files
  __compile $SRC_DIR
  if [ $? -eq 0 ]; then
    __compile $TEST_DIR
  else
    exit 1
  fi
else
  exit 1
fi

exit $?
