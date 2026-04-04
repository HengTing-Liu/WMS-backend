package com.abtk.product;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service 层单元测试基类
 *
 * <p>使用说明：
 * <pre>
 * // 1. 简单 Service 测试（无需 Spring 容器）
 * {@code @ExtendWith(MockitoExtension.class)}
 * class WarehouseServiceTest extends BaseServiceTest {
 *     {@code @Mock}
 *     private WarehouseReceiverMapper mapper;
 *
 *     {@code @InjectMocks}
 *     private WarehouseReceiverServiceImpl service;
 *
 *     {@code @Test}
 *     void testCreate() {
 *         // given
 *         WarehouseReceiver receiver = WarehouseReceiver.builder()
 *             .warehouseCode("WH001")
 *             .consignee("张三")
 *             .build();
 *         when(mapper.insert(any())).thenReturn(1);
 *
 *         // when
 *         Long id = service.create(receiver);
 *
 *         // then
 *         assertNotNull(id);
 *         verify(mapper).insert(any());
 *     }
 * }
 * </pre>
 *
 * <pre>
 * // 2. 复杂 Service 测试（需要 Spring 容器）
 * {@code @SpringBootTest}
 * class WarehouseServiceIntegrationTest extends BaseServiceTest {
 *     {@code @Autowired}
 *     private WarehouseReceiverService service;
 *
 *     {@code @Test}
 *     void testCreateWithTransaction() {
 *         // 真实集成测试
 *     }
 * }
 * </pre>
 *
 * @author wms-test
 */
@ExtendWith(MockitoExtension.class)
public abstract class BaseServiceTest {

    /**
     * 日志记录器（子类可直接使用）
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 打印测试开始日志
     */
    protected void logTestStart(String testName) {
        log.info("========== [{}] 开始 ==========", testName);
    }

    /**
     * 打印测试结束日志
     */
    protected void logTestEnd(String testName) {
        log.info("========== [{}] 结束 ==========", testName);
    }

    /**
     * 打印测试成功日志
     */
    protected void logTestSuccess(String testName) {
        log.info("========== [{}] 成功 ==========", testName);
    }

    /**
     * 打印测试失败日志
     */
    protected void logTestFailed(String testName, Throwable e) {
        log.error("========== [{}] 失败 ==========", testName, e);
    }
}
