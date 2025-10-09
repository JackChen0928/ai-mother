package com.yupi.yuaicodemother.model.dto.deploy;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppDeployRequest implements Serializable {

    /**
     * 应用 id
     */
    private Long appId;

    private static final long serialVersionUID = 1L;
}
