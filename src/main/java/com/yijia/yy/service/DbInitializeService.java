package com.yijia.yy.service;

import java.io.IOException;

/**
 * Initialize static data
 */
public interface DbInitializeService {

    void loadDataIntoDB() throws IOException;
}
