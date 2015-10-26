SRC_DIR=$1
TEST_DIR=$2
OUTPUT_DIR=$3
CLASSPATH=$4

__clean() {
  if [ -d "$OUTPUT_DIR" ]; then
    working_dir=$(pwd)
    cd "$OUTPUT_DIR"
    rm -fr *
    cd "$working_dir"
  fi
}

__compile() {
  if [ ! -d "$1" ]; then
    mkdir "$1"
  fi

  javac -classpath "$JAR_CLASSPATH$OUTPUT_DIR" -d "$OUTPUT_DIR" $(find "$1" -iregex .*\.java)
}

# gather libraries, appends a : to the end
JAR_CLASSPATH=$(find "$CLASSPATH" -iregex .*\.jar | tr '\n' ' ' |  sed 's/[ ][ ]*/:/g')
# compile all source files
if [ $? -eq 0 ]; then
  __clean $OUTPUT_DIR
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
