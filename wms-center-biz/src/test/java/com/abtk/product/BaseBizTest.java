package com.abtk.product;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Biz 层单元测试基类
 *
 * <p>使用说明：
 * <pre>
 * {@code @ExtendWith(MockitoExtension.class)}
 * class WarehouseReceiverBizTest extends BaseBizTest {
 *     {@code @Mock}
 *     private WarehouseReceiverService warehouseReceiverService;
 *
 *     {@code @InjectMocks}
 *     private WarehouseReceiverBiz biz;
 *
 *     {@code @Test}
 *     void testAdd() {
 *         // given
 *         WarehouseReceiverRequest request = new WarehouseReceiverRequest();
 *         request.setWarehouseCode("WH001");
 *         request.setConsignee("张三");
 *         request.setPhoneNumber("13800138000");
 *         request.setIsDefault(1);
 *
 *         when(warehouseReceiverService.create(any())).thenReturn(1L);
 *
 *         // when
 *         R<Long> result = biz.add(request);
 *
 *         // then
 *         assertTrue(result.isSuccess());
 *         assertEquals(1L, result.getData());
 *     }
 * }
 * </pre>
 *
 * @author wms-test
 */
@ExtendWith(MockitoExtension.class)
public abstract class BaseBizTest {

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
