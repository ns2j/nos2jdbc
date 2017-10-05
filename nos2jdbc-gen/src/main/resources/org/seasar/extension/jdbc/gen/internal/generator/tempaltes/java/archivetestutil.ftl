<#if packageName??>
package ${packageName};
</#if>

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class ArchiveTestUtil {
    static String ROOT_PACKAGE_NAME = "${packageName}"; 
    static Archive<?> arch = null;
    static public synchronized Archive<?> createTestArchive() {
	if (arch != null) return arch;
	arch = ShrinkWrap.create(WebArchive.class)
		.addPackages(true, ROOT_PACKAGE_NAME)
		.addAsLibraries(
			Maven.resolver()
			.loadPomFromFile("pom.xml")
			.importRuntimeDependencies()
			.resolve()
			.withTransitivity()
			.asFile());
	return arch;
    }
}
