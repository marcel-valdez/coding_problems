@dir_path = File.dirname(__FILE__)
@output_dir = "#{@dir_path}/classes/"
@src_dir = "#{@dir_path}/src/"
@test_dir = "#{@dir_path}/test/"
@lib_dir = "#{@dir_path}/lib/"
@compile_cmd = "#{@dir_path}/compile.sh #{@src_dir} #{@test_dir} #{@output_dir} #{@lib_dir}"
@test_cmd = "#{@dir_path}/test.sh #{@output_dir} #{@lib_dir}"


guard :test do
  watch(%r|^src/(.*).java$|) do |match|
    executeTest("#{match[1]}Test")
  end


  watch(%r|^test/(.*Test).java$|) do |match|
    executeTest(match[1])
  end

  watch(%r|^test/(RunJunit).java$|) do |match|
    executeTest(match[1])
  end

  def executeTest(testName)
    puts `#{@compile_cmd}`
    if $?.success?
      puts `#{@test_cmd} #{testName}`
    end
  end
end

