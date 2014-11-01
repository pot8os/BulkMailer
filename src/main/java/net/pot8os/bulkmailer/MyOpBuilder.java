package net.pot8os.bulkmailer;

import org.apache.commons.cli.Option;

/**
 * re-created OptionBuilder
 * @author pot8os
 */
public class MyOpBuilder {

    private String longOpt, description, argName;
    private boolean required, optionalArg;
    private int numberOfArgs = Option.UNINITIALIZED;
    private Object type;
    private char valuesep;

    public MyOpBuilder() {
    }

    public MyOpBuilder withLongOpt(String newLongOpt) {
        this.longOpt = newLongOpt;
        return this;
    }

    public MyOpBuilder hasArg() {
        this.numberOfArgs = 1;
        return this;
    }

    public MyOpBuilder hasArg(boolean hasArg) {
        this.numberOfArgs = hasArg ? 1 : Option.UNINITIALIZED;
        return this;
    }

    public MyOpBuilder withArgName(String name) {
        this.argName = name;
        return this;
    }

    public MyOpBuilder isRequired() {
        this.required = true;
        return this;
    }

    public MyOpBuilder withValueSeparator(char sep) {
        this.valuesep = sep;
        return this;
    }

    public MyOpBuilder withValueSeparator() {
        this.valuesep = '=';
        return this;
    }

    public MyOpBuilder isRequired(boolean newRequired) {
        this.required = newRequired;
        return this;
    }

    public MyOpBuilder hasArgs() {
        this.numberOfArgs = Option.UNLIMITED_VALUES;
        return this;
    }

    public MyOpBuilder hasArgs(int num) {
        this.numberOfArgs = num;
        return this;
    }

    public MyOpBuilder hasOptionalArg() {
        this.numberOfArgs = 1;
        this.optionalArg = true;
        return this;
    }

    public MyOpBuilder hasOptionalArgs() {
        this.numberOfArgs = Option.UNLIMITED_VALUES;
        this.optionalArg = true;
        return this;
    }

    public MyOpBuilder hasOptionalArgs(int numArgs) {
        this.numberOfArgs = numArgs;
        this.optionalArg = true;
        return this;
    }

    public MyOpBuilder withType(Object newType) {
        this.type = newType;
        return this;
    }

    public MyOpBuilder withDescription(String newDescription) {
        this.description = newDescription;
        return this;
    }

    public Option create(char opt) {
        return create(String.valueOf(opt));
    }

    public Option create() {
        if (this.longOpt == null) {
            throw new IllegalArgumentException("must sepcify longOpt");
        }
        return create(null);
    }

    public Option create(String opt) {
        // create the option
        Option option = new Option(opt, description);
        // set the option properties
        option.setLongOpt(longOpt);
        option.setRequired(required);
        option.setOptionalArg(optionalArg);
        option.setArgs(numberOfArgs);
        option.setType(type);
        option.setValueSeparator(valuesep);
        option.setArgName(argName);
        // return the Option instance
        return option;
    }
}
