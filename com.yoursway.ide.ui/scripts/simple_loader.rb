# 
# This script fetches information about the requested gem and it's .
#
# ARGV[0] contains name of gem, ARGV[1], optonal, contains requested version of
# it.
#
# * Protocol *
#
# Succesful execution: exit code 0.
# Output contains set of YAML documents, each of form:
# -
#   - name: <name>
#   - version: <version>
#   - directory: <installation directory>
#   - require_path:
#      - <path>
#      - <path>
#      ...
#      - <path>
#
# If specific version of gem has been requested, gem and all its dependencies are returned.
# If gem was requested without a version, all versions of this gema are returned.
#
# Errors:
#
# * No gem passed to program: exit code 1.
#   Output contains YAML document
#
#   no_input_gem:
#
# * Gem is not found: exit code 2.
#   Output contains YAML document
#
#   gem_not_found: <gem name>
#
# * No rubygems found: exit code 3.
#   Output contains Yaml document
#
#   no_rubygems:
#
# * Unexpected exception thrown during execution: exit code 255.
#   Stdout contains the stack trace.
#

def error_exit(return_code, error_message)
    $stdout.print(error_message.to_yaml)
    exit(return_code)
end

def dump_gem(gem)
  { "name" => gem.name,
    "version" => gem.version.to_s,
    "directory" => gem.full_gem_path,
    "require_path" => gem.require_paths
  }
end

class GemsWalker
  def initialize
    @source_index = Gem::SourceIndex.from_installed_gems
    @gems = {}
  end

  def walk!(gem_name, gem_requirements)
    return if @gems.has_key? gem_name

    gem = @source_index.search(gem_name, gem_requirements)
    error_exit(2, {"gem_not_found" => gem_name}) if gem.empty?
    @gems[gem_name] = gem[0]

    gem[0].dependencies.each { |dep| walk!(dep.name, dep.version_requirements) }
  end

  def search_all(gem_name)
    @source_index.search(gem_name).map { |gem| dump_gem(gem) }
  end

  def dump_gems
    @gems.map { |gem_name, gem| dump_gem(gem) }
  end
end

begin
  require "yaml"

  begin
    require "rubygems"
  rescue LoadError
    error_exit(3, {"no_rubygems" => nil})
  end

  error_exit(1, {"no_input_gem" => nil}) if ARGV.size == 0

  gem_name = ARGV[0]

  walker = GemsWalker.new
  if ARGV.size > 1
    walker.walk!(gem_name, ARGV[1])
    $stdout.print(walker.dump_gems.to_yaml)
  else
    $stdout.print(walker.search_all(gem_name).to_yaml)
  end

rescue SystemExit
  raise
rescue Exception => e
  print "#{e.class}: #{e}\n"
  e.backtrace.each { |trace| print "        #{trace}\n" }
  exit(255)
end
