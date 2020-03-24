package com.imooc.service;

import java.util.List;

public interface EasyUiTreeService {
    /**
     * 根据父id找所有的子节点id
     * @param id
     * @return
     */
    public List<String> getIdsByParentId(String id);

    /**
     * 测试用例
     * @return
     */
    public List exemple();


}
