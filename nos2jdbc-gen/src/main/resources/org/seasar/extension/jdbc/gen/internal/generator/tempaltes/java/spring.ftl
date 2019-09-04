<#macro importForService>
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
</#macro>
<#macro annotationForService>
@Service
@Transactional
</#macro>

<#macro importForTest>
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
</#macro>
