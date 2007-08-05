package com.yoursway.rails.commons;

import org.eclipse.core.runtime.Path;

import com.yoursway.common.SegmentedName;
import com.yoursway.common.StringUtils;

public class RailsNamingConventions {
    
    public static final String DB_SCHEMA_RB = "db/schema.rb";
    
    public static final Path DB_SCHEMA_RB_PATH = new Path(DB_SCHEMA_RB);
    
    public static final String APP = "app";
    
    public static final Path APP_PATH = new Path(APP);
    
    public static final String APP_CONTROLLERS = "app/controllers";
    
    public static final Path APP_CONTROLLERS_PATH = new Path(APP_CONTROLLERS);
    
    public static final String APP_MODELS = "app/models";
    
    public static final Path APP_MODELS_PATH = new Path(APP_MODELS);
    
    public static final String TEST_FIXTURES = "test/fixtures";
    
    public static final Path TEST_FIXTURES_PATH = new Path(TEST_FIXTURES);
    
    public static final String TEST_UNIT = "test/unit";
    
    public static final Path TEST_UNIT_PATH = new Path(TEST_UNIT);
    
    public static final String TEST_FUNCTIONAL = "test/functional";
    
    public static final Path TEST_FUNCTIONAL_PATH = new Path(TEST_UNIT);
    
    public static final String DB_MIGRATIONS = "db/migrate";
    
    public static final Path DB_MIGRATIONS_PATH = new Path(DB_MIGRATIONS);
    
    private static final int IS_START = -1;
    
    private static final int IS_NONE = 0;
    
    private static final int IS_UPPER = 1;
    
    private static final int IS_LOWER = 2;
    
    private static final int IS_DIGIT = 3;
    
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
    
    public static SegmentedName camelize(SegmentedName name) {
        return new SegmentedName(camelize(name.getSegments()));
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
    
    public static String[] splitPath(String name) {
        return name.split("/");
    }
    
    public static String joinNamespaces(SegmentedName name) {
        return joinNamespaces(name.getSegments());
    }
    
    public static String joinNamespaces(String[] name) {
        return StringUtils.join(name, "::");
    }
    
    //    # Examples
    //    #   "RawScaledScorer".tableize #=> "raw_scaled_scorers"
    //    #   "egg_and_ham".tableize #=> "egg_and_hams"
    //    #   "fancyCategory".tableize #=> "fancy_categories"
    public static String tableize(Inflector inflector, String className) {
        return inflector.pluralize(underscore(className));
    }
    
    public static String classify(Inflector inflector, String tableName) {
        int pos = tableName.lastIndexOf('.');
        if (pos >= 0)
            tableName = tableName.substring(pos + 1);
        return camelize(inflector.singularize(tableName));
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
    
    public static Inflector createInitializedInflector() {
        Inflector inflect = new Inflector();
        inflect.plural("$", "s");
        inflect.plural("s$", "s");
        inflect.plural("(ax|test)is$", "\\1es");
        inflect.plural("(octop|vir)us$", "\\1i");
        inflect.plural("(alias|status)$", "\\1es");
        inflect.plural("(bu)s$", "\\1ses");
        inflect.plural("(buffal|tomat)o$", "\\1oes");
        inflect.plural("([ti])um$", "\\1a");
        inflect.plural("sis$", "ses");
        inflect.plural("(?:([^f])fe|([lr])f)$", "\\1\\2ves");
        inflect.plural("(hive)$", "\\1s");
        inflect.plural("([^aeiouy]|qu)y$", "\\1ies");
        inflect.plural("(x|ch|ss|sh)$", "\\1es");
        inflect.plural("(matr|vert|ind)ix|ex$", "\\1ices");
        inflect.plural("([m|l])ouse$", "\\1ice");
        inflect.plural("^(ox)$", "\\1en");
        inflect.plural("(quiz)$", "\\1zes");
        
        inflect.singular("s$", "");
        inflect.singular("(n)ews$", "\\1ews");
        inflect.singular("([ti])a$", "\\1um");
        inflect.singular("((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$", "\\1\\2sis");
        inflect.singular("(^analy)ses$", "\\1sis");
        inflect.singular("([^f])ves$", "\\1fe");
        inflect.singular("(hive)s$", "\\1");
        inflect.singular("(tive)s$", "\\1");
        inflect.singular("([lr])ves$", "\\1f");
        inflect.singular("([^aeiouy]|qu)ies$", "\\1y");
        inflect.singular("(s)eries$", "\\1eries");
        inflect.singular("(m)ovies$", "\\1ovie");
        inflect.singular("(x|ch|ss|sh)es$", "\\1");
        inflect.singular("([m|l])ice$", "\\1ouse");
        inflect.singular("(bus)es$", "\\1");
        inflect.singular("(o)es$", "\\1");
        inflect.singular("(shoe)s$", "\\1");
        inflect.singular("(cris|ax|test)es$", "\\1is");
        inflect.singular("(octop|vir)i$", "\\1us");
        inflect.singular("(alias|status)es$", "\\1");
        inflect.singular("^(ox)en", "\\1");
        inflect.singular("(vert|ind)ices$", "\\1ex");
        inflect.singular("(matr)ices$", "\\1ix");
        inflect.singular("(quiz)zes$", "\\1");
        
        inflect.irregular("person", "people");
        inflect.irregular("man", "men");
        inflect.irregular("child", "children");
        inflect.irregular("sex", "sexes");
        inflect.irregular("move", "moves");
        
        inflect.uncountable("equipment");
        inflect.uncountable("information");
        inflect.uncountable("rice");
        inflect.uncountable("money");
        inflect.uncountable("species");
        inflect.uncountable("series");
        inflect.uncountable("fish");
        inflect.uncountable("sheep");
        
        return inflect;
    }
    
}
