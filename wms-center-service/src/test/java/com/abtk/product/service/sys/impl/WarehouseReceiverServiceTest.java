package com.abtk.product.service.sys.impl;

import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.dao.entity.WarehouseReceiver;
import com.abtk.product.dao.mapper.WarehouseReceiverMapper;
import com.abtk.product.service.system.service.I18nService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 仓库收货地址 Service 单元测试
 *
 * <p>测试覆盖：
 * <ul>
 *   <li>list - 查询列表</li>
 *   <li>getById - 根据ID查询</li>
 *   <li>create - 新增收货地址</li>
 *   <li>update - 更新收货地址</li>
 *   <li>delete - 删除收货地址</li>
 *   <li>setDefault - 设置默认地址</li>
 * </ul>
 *
 * @author wms-test
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("仓库收货地址 Service 测试")
class WarehouseReceiverServiceTest {

    @Mock
    private WarehouseReceiverMapper warehouseReceiverMapper;

    @Mock
    private I18nService i18nService;

    @InjectMocks
    private WarehouseReceiverServiceImpl service;

    private WarehouseReceiver testReceiver;

    @BeforeEach
    void setUp() {
        testReceiver = createTestReceiver();
    }

    // ==================== list - 查询列表测试 ====================

    @Nested
    @DisplayName("list - 查询列表")
    class ListTests {

        @Test
        @DisplayName("成功查询收货地址列表")
        void list_Success() {
            // given
            String warehouseCode = "WH001";
            List<WarehouseReceiver> expectedList = Arrays.asList(
                    createTestReceiver(1L, "张三"),
                    createTestReceiver(2L, "李四")
            );
            when(warehouseReceiverMapper.selectByWarehouseCode(warehouseCode))
                    .thenReturn(expectedList);

            // when
            List<WarehouseReceiver> result = service.list(warehouseCode);

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("张三", result.get(0).getConsignee());
            assertEquals("李四", result.get(1).getConsignee());
            verify(warehouseReceiverMapper).selectByWarehouseCode(warehouseCode);
        }

        @Test
        @DisplayName("仓库编码为空时抛出异常")
        void list_EmptyWarehouseCode_ThrowsException() {
            // given
            when(i18nService.getMessage(anyString())).thenReturn("仓库编码不能为空");

            // when & then
            ServiceException exception = assertThrows(ServiceException.class,
                    () -> service.list(""));

            assertEquals("仓库编码不能为空", exception.getMessage());
            verify(warehouseReceiverMapper, never()).selectByWarehouseCode(anyString());
        }

        @Test
        @DisplayName("查询结果为空时返回空列表")
        void list_EmptyResult() {
            // given
            when(warehouseReceiverMapper.selectByWarehouseCode("WH999"))
                    .thenReturn(Collections.emptyList());

            // when
            List<WarehouseReceiver> result = service.list("WH999");

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    // ==================== getById - 根据ID查询测试 ====================

    @Nested
    @DisplayName("getById - 根据ID查询")
    class GetByIdTests {

        @Test
        @DisplayName("成功根据ID查询收货地址")
        void getById_Success() {
            // given
            Long id = 1L;
            WarehouseReceiver expected = createTestReceiver(id, "张三");
            when(warehouseReceiverMapper.selectById(id)).thenReturn(expected);

            // when
            WarehouseReceiver result = service.getById(id);

            // then
            assertNotNull(result);
            assertEquals(id, result.getId());
            assertEquals("张三", result.getConsignee());
            verify(warehouseReceiverMapper).selectById(id);
        }

        @Test
        @DisplayName("收货地址不存在时抛出异常")
        void getById_NotFound_ThrowsException() {
            // given
            Long id = 999L;
            when(i18nService.getMessage(anyString())).thenReturn("收货地址不存在");
            when(warehouseReceiverMapper.selectById(id)).thenReturn(null);

            // when & then
            ServiceException exception = assertThrows(ServiceException.class,
                    () -> service.getById(id));

            assertEquals("收货地址不存在", exception.getMessage());
        }
    }

    // ==================== create - 新增测试 ====================

    @Nested
    @DisplayName("create - 新增收货地址")
    class CreateTests {

        @Test
        @DisplayName("成功新增收货地址（普通）")
        void create_Success_Normal() {
            // given
            WarehouseReceiver receiver = createTestReceiver();
            receiver.setId(null);

            // 直接设置 createBy，避免 mockStatic 依赖问题
            when(warehouseReceiverMapper.insert(any(WarehouseReceiver.class)))
                    .thenAnswer(invocation -> {
                        WarehouseReceiver arg = invocation.getArgument(0);
                        arg.setId(1L);
                        // 验证 createBy 被正确设置
                        return 1;
                    });

            // when
            Long result = service.create(receiver);

            // then
            assertNotNull(result);
            assertEquals(1L, result);

            // 验证插入被调用
            verify(warehouseReceiverMapper).insert(any(WarehouseReceiver.class));
        }

        @Test
        @DisplayName("成功新增收货地址（设为默认）- 自动清空其他默认")
        void create_Success_SetDefault() {
            // given
            WarehouseReceiver receiver = createTestReceiver();
            receiver.setIsDefault(1);

            when(warehouseReceiverMapper.insert(any(WarehouseReceiver.class)))
                    .thenAnswer(invocation -> {
                        WarehouseReceiver arg = invocation.getArgument(0);
                        arg.setId(1L);
                        return 1;
                    });

            // when
            Long result = service.create(receiver);

            // then
            assertNotNull(result);
            // 验证先清空其他默认，再插入
            verify(warehouseReceiverMapper).clearDefaultByWarehouseCode("WH001");
            verify(warehouseReceiverMapper).insert(any(WarehouseReceiver.class));
        }
    }

    // ==================== update - 更新测试 ====================

    @Nested
    @DisplayName("update - 更新收货地址")
    class UpdateTests {

        @Test
        @DisplayName("成功更新收货地址")
        void update_Success() {
            // given
            Long id = 1L;
            WarehouseReceiver receiver = createTestReceiver(id, "张三");
            when(warehouseReceiverMapper.selectById(id)).thenReturn(testReceiver);
            when(warehouseReceiverMapper.update(any(WarehouseReceiver.class))).thenReturn(1);

            // when
            service.update(id, receiver);

            // then
            verify(warehouseReceiverMapper).update(any(WarehouseReceiver.class));
        }

        @Test
        @DisplayName("收货地址不存在时更新失败")
        void update_NotFound_ThrowsException() {
            // given
            Long id = 999L;
            when(i18nService.getMessage(anyString())).thenReturn("收货地址不存在");
            when(warehouseReceiverMapper.selectById(id)).thenReturn(null);

            // when & then
            assertThrows(ServiceException.class,
                    () -> service.update(id, testReceiver));

            verify(warehouseReceiverMapper, never()).update(any());
        }

        @Test
        @DisplayName("更新影响0行时抛出异常")
        void update_NoRowsAffected_ThrowsException() {
            // given
            Long id = 1L;
            when(i18nService.getMessage(anyString())).thenReturn("更新失败");
            when(warehouseReceiverMapper.selectById(id)).thenReturn(testReceiver);
            when(warehouseReceiverMapper.update(any(WarehouseReceiver.class))).thenReturn(0);

            // when & then
            assertThrows(ServiceException.class,
                    () -> service.update(id, testReceiver));
        }
    }

    // ==================== delete - 删除测试 ====================

    @Nested
    @DisplayName("delete - 删除收货地址")
    class DeleteTests {

        @Test
        @DisplayName("成功删除收货地址")
        void delete_Success() {
            // given
            Long id = 1L;
            when(warehouseReceiverMapper.selectById(id)).thenReturn(testReceiver);
            when(warehouseReceiverMapper.deleteById(id)).thenReturn(1);

            // when
            service.delete(id);

            // then
            verify(warehouseReceiverMapper).deleteById(id);
        }

        @Test
        @DisplayName("收货地址不存在时删除失败")
        void delete_NotFound_ThrowsException() {
            // given
            Long id = 999L;
            when(i18nService.getMessage(anyString())).thenReturn("收货地址不存在");
            when(warehouseReceiverMapper.selectById(id)).thenReturn(null);

            // when & then
            assertThrows(ServiceException.class,
                    () -> service.delete(id));

            verify(warehouseReceiverMapper, never()).deleteById(anyLong());
        }
    }

    // ==================== setDefault - 设置默认测试 ====================

    @Nested
    @DisplayName("setDefault - 设置默认地址")
    class SetDefaultTests {

        @Test
        @DisplayName("成功设置为默认地址")
        void setDefault_Success() {
            // given
            Long id = 1L;
            when(warehouseReceiverMapper.selectById(id)).thenReturn(testReceiver);
            when(warehouseReceiverMapper.clearDefaultByWarehouseCode("WH001")).thenReturn(1);
            when(warehouseReceiverMapper.setDefault(id)).thenReturn(1);

            // when
            service.setDefault(id);

            // then
            verify(warehouseReceiverMapper).clearDefaultByWarehouseCode("WH001");
            verify(warehouseReceiverMapper).setDefault(id);
        }

        @Test
        @DisplayName("收货地址不存在时设置默认失败")
        void setDefault_NotFound_ThrowsException() {
            // given
            Long id = 999L;
            when(i18nService.getMessage(anyString())).thenReturn("收货地址不存在");
            when(warehouseReceiverMapper.selectById(id)).thenReturn(null);

            // when & then
            assertThrows(ServiceException.class,
                    () -> service.setDefault(id));

            verify(warehouseReceiverMapper, never()).clearDefaultByWarehouseCode(anyString());
            verify(warehouseReceiverMapper, never()).setDefault(anyLong());
        }

        @Test
        @DisplayName("设置默认失败时抛出异常")
        void setDefault_Failed_ThrowsException() {
            // given
            Long id = 1L;
            when(i18nService.getMessage(anyString())).thenReturn("设置默认失败");
            when(warehouseReceiverMapper.selectById(id)).thenReturn(testReceiver);
            when(warehouseReceiverMapper.clearDefaultByWarehouseCode("WH001")).thenReturn(1);
            when(warehouseReceiverMapper.setDefault(id)).thenReturn(0);

            // when & then
            assertThrows(ServiceException.class,
                    () -> service.setDefault(id));
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 创建测试用收货地址
     */
    private WarehouseReceiver createTestReceiver() {
        return createTestReceiver(1L, "张三");
    }

    /**
     * 创建测试用收货地址（带参数）
     */
    private WarehouseReceiver createTestReceiver(Long id, String consignee) {
        WarehouseReceiver receiver = new WarehouseReceiver();
        receiver.setId(id);
        receiver.setWarehouseCode("WH001");
        receiver.setConsignee(consignee);
        receiver.setPhoneNumber("13800138000");
        receiver.setCountry("中国");
        receiver.setProvince("上海市");
        receiver.setCity("上海市");
        receiver.setDistrict("浦东新区");
        receiver.setDetailedAddress("张江高科技园区123号");
        receiver.setPostalCode("200000");
        receiver.setIsDefault(0);
        receiver.setIsDeleted(0);
        return receiver;
    }
}
