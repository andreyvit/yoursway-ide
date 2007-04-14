package com.yoursway.utils;

public class RailsNamingConventions {
    
    private static final int IS_START = -1;
    
    private static final int IS_NONE = 0;
    
    private static final int IS_UPPER = 1;
    
    private static final int IS_LOWER = 2;
    
    private static final int IS_DIGIT = 3;
    
    public class Inflector {
        
    }
    
    public static String controllerNameToPath(String controllerName) {
        return underscore(StringUtils.stripSuffix(controllerName, "Controller"));
    }
    
    public static String[] controllerNameToPath(String[] controllerName) {
        int length = controllerName.length;
        String[] result = new String[length];
        for (int i = 0; i < length - 1; i++)
            result[i] = underscore(controllerName[i]);
        result[length - 1] = controllerNameToPath(controllerName[length - 1]);
        return result;
    }
    
    /**
     * Returns the plural form of the word in the string. Examples:
     * <ul>
     * <li><code>"post".pluralize #=> "posts"</code></li>
     * <li><code>"octopus".pluralize #=> "octopi"</code></li>
     * <li><code>"sheep".pluralize #=> "sheep"</code></li>
     * <li><code>"the blue mailman".pluralize #=> "the blue mailmen"</code></li>
     * <li><code> "CamelOctopus".pluralize #=> "CamelOctopi"</code></li>
     * </ul>
     * 
     * @param word
     * @return
     */
    public static String pluralize(String word) {
        throw new UnsupportedOperationException();
        //      result = word.to_s.dup
        //
        //      if inflections.uncountables.include?(result.downcase)
        //        result
        //      else
        //        inflections.plurals.each { |(rule, replacement)| break if result.gsub!(rule, replacement) }
        //        result
        //      end
    }
    
    //    # Examples
    //    #   "posts".singularize #=> "post"
    //    #   "octopi".singularize #=> "octopus"
    //    #   "sheep".singluarize #=> "sheep"
    //    #   "word".singluarize #=> "word"
    //    #   "the blue mailmen".singularize #=> "the blue mailman"
    //    #   "CamelOctopi".singularize #=> "CamelOctopus"
    public static String singularize(String word) {
        throw new UnsupportedOperationException();
        //      result = word.to_s.dup
        //
        //      if inflections.uncountables.include?(result.downcase)
        //        result
        //      else
        //        inflections.singulars.each { |(rule, replacement)| break if result.gsub!(rule, replacement) }
        //        result
        //      end
    }
    
    //    # Examples
    //    #   "active_record".camelize #=> "ActiveRecord"
    //    #   "active_record/errors".camelize #=> "ActiveRecord::Errors"
    public static String camelize(String lowerCaseAndUnderscoredWord) {
        StringBuilder result = new StringBuilder(lowerCaseAndUnderscoredWord);
        int length = result.length();
        boolean uppercaseNext = true;
        for (int i = 0; i < length; i++) {
            char ch = result.charAt(i);
            if (ch == '_') {
                uppercaseNext = true;
                result.deleteCharAt(i);
                i--;
                length--;
            } else if (!Character.isLetterOrDigit(ch))
                uppercaseNext = true;
            else {
                if (uppercaseNext)
                    result.setCharAt(i, Character.toUpperCase(ch));
                uppercaseNext = false;
            }
        }
        return result.toString();
    }
    
    public static String[] camelize(String[] lowerCaseAndUnderscoredWords) {
        String[] result = new String[lowerCaseAndUnderscoredWords.length];
        for (int i = 0; i < lowerCaseAndUnderscoredWords.length; i++) {
            result[i] = camelize(lowerCaseAndUnderscoredWords[i]);
        }
        return result;
    }
    
    //    # Examples
    //    #   "active_record".camelize #=> "activeRecord"
    //    #   "active_record/errors".camelize #=> "activeRecord::Errors"
    public static String camelizeLower(String lower_case_and_underscored_word, boolean firstLetterInUpperCase) {
        return lower_case_and_underscored_word.substring(0, 1)
                + camelize(lower_case_and_underscored_word).substring(1);
    }
    
    //    # The reverse of +camelize+. Makes an underscored form from the expression in the string.
    //    #
    //    # Examples
    //    #   "ActiveRecord".underscore #=> "active_record"
    //    #   "RubyASTBuilder".underscore #=> "ruby_ast_builder"
    //    #   "ActiveRecord::Errors".underscore #=> active_record/errors
    public static String underscore(String camelCasedWord) {
        StringBuilder result = new StringBuilder(camelCasedWord.length() * 2);
        int prev = IS_START, prevprev = IS_START;
        int length = camelCasedWord.length();
        for (int i = 0; i < length; i++) {
            char ch = camelCasedWord.charAt(i);
            int cur = classify(ch);
            if (ch == '-')
                result.append('_');
            else if (cur == IS_UPPER && (prev == IS_LOWER || prev == IS_DIGIT))
                result.append('_').append(Character.toLowerCase(ch));
            else if (cur == IS_LOWER && prev == IS_UPPER && prevprev == IS_UPPER)
                result.insert(result.length() - 1, '_').append(ch);
            else if (cur == IS_UPPER)
                result.append(Character.toLowerCase(ch));
            else
                result.append(ch);
            prevprev = prev;
            prev = cur;
        }
        return result.toString();
    }
    
    public static String[] underscore(String[] camelCasedWords) {
        String[] result = new String[camelCasedWords.length];
        for (int i = 0; i < camelCasedWords.length; i++) {
            result[i] = underscore(camelCasedWords[i]);
        }
        return result;
    }
    
    private static int classify(char ch) {
        if (Character.isUpperCase(ch))
            return IS_UPPER;
        if (Character.isLowerCase(ch))
            return IS_LOWER;
        if (Character.isDigit(ch))
            return IS_DIGIT;
        return IS_NONE;
    }
    
    public static String[] splitNamespaces(String name) {
        return name.split("::");
    }
    
    public static String joinNamespaces(String[] name) {
        return StringUtils.join(name, "::");
    }
    
    //    # Examples
    //    #   "RawScaledScorer".tableize #=> "raw_scaled_scorers"
    //    #   "egg_and_ham".tableize #=> "egg_and_hams"
    //    #   "fancyCategory".tableize #=> "fancy_categories"
    public static String tableize(String className) {
        return pluralize(underscore(className));
    }
    
    public static String classify(String tableName) {
        int pos = tableName.lastIndexOf('.');
        if (pos >= 0)
            tableName = tableName.substring(pos + 1);
        return camelize(singularize(tableName));
    }
    
    public static String demodulize(String className) {
        int pos = className.lastIndexOf("::");
        if (pos >= 0)
            return className.substring(pos + 2);
        else
            return className;
    }
    
    //    # Creates a foreign key name from a class name.
    //    # +separate_class_name_and_id_with_underscore+ sets whether
    //    # the method should put '_' between the name and 'id'.
    //    #
    //    # Examples
    //    #   "Message".foreign_key #=> "message_id"
    //    #   "Message".foreign_key(false) #=> "messageid"
    //    #   "Admin::Post".foreign_key #=> "post_id"
    public static String foreignKey(String className, boolean separateIdWithUnderscore) {
        return underscore(demodulize(className)) + (separateIdWithUnderscore ? "_id" : "id");
    }
    
    //    # A singleton instance of this class is yielded by Inflector.inflections, which can then be used to specify additional
    //    # inflection rules. Examples:
    //    #
    //    #   Inflector.inflections do |inflect|
    //    #     inflect.plural /^(ox)$/i, '\1\2en'
    //    #     inflect.singular /^(ox)en/i, '\1'
    //    #
    //    #     inflect.irregular 'octopus', 'octopi'
    //    #
    //    #     inflect.uncountable "equipment"
    //    #   end
    //    #
    //    # New rules are added at the top. So in the example above, the irregular rule for octopus will now be the first of the
    //    # pluralization and singularization rules that is runs. This guarantees that your rules run before any of the rules that may
    //    # already have been loaded.
    //    class Inflections
    //      include Singleton
    //
    //      attr_reader :plurals, :singulars, :uncountables
    //
    //      def initialize
    //        @plurals, @singulars, @uncountables = [], [], []
    //      end
    //
    //      # Specifies a new pluralization rule and its replacement. The rule can either be a string or a regular expression.
    //      # The replacement should always be a string that may include references to the matched data from the rule.
    //      def plural(rule, replacement)
    //        @plurals.insert(0, [rule, replacement])
    //      end
    //
    //      # Specifies a new singularization rule and its replacement. The rule can either be a string or a regular expression.
    //      # The replacement should always be a string that may include references to the matched data from the rule.
    //      def singular(rule, replacement)
    //        @singulars.insert(0, [rule, replacement])
    //      end
    //
    //      # Specifies a new irregular that applies to both pluralization and singularization at the same time. This can only be used
    //      # for strings, not regular expressions. You simply pass the irregular in singular and plural form.
    //      #
    //      # Examples:
    //      #   irregular 'octopus', 'octopi'
    //      #   irregular 'person', 'people'
    //      def irregular(singular, plural)
    //        plural(Regexp.new("(#{singular[0,1]})#{singular[1..-1]}$", "i"), '\1' + plural[1..-1])
    //        singular(Regexp.new("(#{plural[0,1]})#{plural[1..-1]}$", "i"), '\1' + singular[1..-1])
    //      end
    //
    //      # Add uncountable words that shouldn't be attempted inflected.
    //      #
    //      # Examples:
    //      #   uncountable "money"
    //      #   uncountable "money", "information"
    //      #   uncountable %w( money information rice )
    //      def uncountable(*words)
    //        (@uncountables << words).flatten!
    //      end
    //
    //      # Clears the loaded inflections within a given scope (default is :all). Give the scope as a symbol of the inflection type,
    //      # the options are: :plurals, :singulars, :uncountables
    //      #
    //      # Examples:
    //      #   clear :all
    //      #   clear :plurals
    //      def clear(scope = :all)
    //        case scope
    //          when :all
    //            @plurals, @singulars, @uncountables = [], [], []
    //          else
    //            instance_variable_set "@#{scope}", []
    //        end
    //      end
    //    end
    //
    //    extend self
    //
    
}
