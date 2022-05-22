package org.seasar.extension.jdbc.gen.internal.command;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.seasar.extension.jdbc.gen.internal.util.VersionUtil;
import org.seasar.framework.conversion.StringConversionUtil;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.FileUtil;

public class CopyTo0000Command extends AbstractCommand {
    /** ロガー */
    protected static Logger logger = Logger.getLogger(CopyTo0000Command.class);

    @Override protected void doValidate() {}
    
    @Override protected void doInit() {}

    @Override
    protected void doExecute() throws Throwable {
        String versionName = StringConversionUtil
                .toString(VersionUtil.getVersionNo(), "0000");
        logger.info("current version: " + versionName);
        Path destPath = Paths.get("db", "0000");
        FileUtil.rmdir(destPath);
        FileUtil.copyFolder(Paths.get("db", "migrate", versionName), destPath, StandardCopyOption.REPLACE_EXISTING);
    }
    
    @Override protected void doDestroy() {}

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
