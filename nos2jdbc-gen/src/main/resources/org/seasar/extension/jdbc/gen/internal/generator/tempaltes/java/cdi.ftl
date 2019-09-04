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
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;

import ${rootPackageName}.ArchiveTestUtil;
</#macro>

