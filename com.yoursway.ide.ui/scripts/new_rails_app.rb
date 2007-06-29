# copied from /usr/local/rails
require 'rubygems'
raise "Version of Rails must be specified as the first argument" if ARGV[0].nil?
version = ARGV[0]
ARGV.shift
require_gem 'rails', version
puts "ARGV[0] = #{ARGV[0]}"
load 'rails'
