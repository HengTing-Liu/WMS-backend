package com.abtk.product.common.utils;

import com.abtk.product.common.page.PageRequest;
import com.abtk.product.common.page.PageResult;
import com.abtk.product.common.web.page.TableDataInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("resource")
public class PageUtil {
    public static <T> PageResult<T> page(PageRequest req, Supplier<List<T>> query) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<T> list = query.get();
        PageInfo<T> info = new PageInfo<>(list);
        return new PageResult<>(info.getPageNum(), info.getPageSize(),info.getTotal(),info.getPageNum(),info.getList());
    }

    public static <T, R> PageResult<R> page(PageRequest req,
                                            Supplier<List<T>> query,
                                            Function<? super T, ? extends R> mapper) {
        PageResult<T> origin = page(req, query);
        List<R> newList = origin.getList().stream().map(mapper).collect(Collectors.toList());

        PageResult<R> r = new PageResult<>();
        r.setTotal(origin.getTotal());
        r.setPageNum(origin.getPageNum());
        r.setPageSize(origin.getPageSize());
        r.setList(newList);
        r.setTotalPages(origin.getTotalPages());
        return r;
    }

    /**
     * 将实体列表转换为 VO/DTO 列表，并封装成分页结果
     *
     * @param entityList 实体列表（如 List<SysUser>）
     * @param converter  转换函数（如 SysUserConverter.INSTANCE::toResponse）
     * @param <T>        源实体类型
     * @param <R>        目标 VO/DTO 类型
     * @return 分页数据对象
     */
    public static <T, R> TableDataInfo convertPage(List<T> entityList, Function<T, R> converter) {
        TableDataInfo result = new TableDataInfo();
        // 转换列表
        List<R> voList = entityList.stream()
                .map(converter)
                .collect(Collectors.toList());
        result.setRows(voList);
        result.setTotal(new PageInfo<>(entityList).getTotal());
        return result;
    }
}
