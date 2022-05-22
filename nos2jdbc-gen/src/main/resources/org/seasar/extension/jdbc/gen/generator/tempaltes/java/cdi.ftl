<#macro importForService>
import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;
</#macro>
<#macro annotationForService>
@Dependent
@Transactional
@${serviceClassNameSuffix}Qualifier
</#macro>

<#macro importForTest>
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import ${rootPackageName}.ArchiveTestUtil;
</#macro>

