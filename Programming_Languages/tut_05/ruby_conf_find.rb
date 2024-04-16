#!/usr/bin/env ruby

directory = "/"

#get all the files in the directory and its sub-directories ending with ".conf"
#files = Dir[directory + "/**/*.conf"]
files = Dir.glob(directory + "/**/*.conf")

puts "Number of config files: #{files.length}"


