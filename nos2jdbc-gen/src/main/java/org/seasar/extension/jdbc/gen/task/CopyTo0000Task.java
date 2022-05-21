package org.seasar.extension.jdbc.gen.task;

import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.internal.command.CopyTo0000Command;

public class CopyTo0000Task extends AbstractTask {

    @Override
    protected Command getCommand() {
        return new CopyTo0000Command();
    }

}
