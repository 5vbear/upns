package com.sirius.upns.server.router.repository.impl;

import com.google.common.collect.Lists;
import com.sirius.upns.server.router.domain.model.Router;
import com.sirius.upns.server.router.repository.RouterRepository;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

/**
 * Created by pippo on 14-3-22.
 */
@Repository
public class RouterRepositoryImpl implements RouterRepository {


    @Value("${ctu.upns.server.router.address}")
    private String addressList;

    private Router[] servers;

    private Random random;

    @PostConstruct
    public void init() {
        if (addressList.endsWith(";")) {
            addressList = addressList.substring(0, addressList.length() - 1);
        }

        List<Router> _servers = Lists.newArrayList();

        for (String address : addressList.split(";")) {
            String[] info = address.split(":");
            Validate.isTrue(info.length == 2, "invalid address:[%s]", address);
            _servers.add(new Router(info[0], Integer.valueOf(info[1])));
        }

        servers = _servers.toArray(new Router[_servers.size()]);
        Validate.notEmpty(servers, "the servers can not be null!");

        random = new Random(System.currentTimeMillis());
    }

    @Override
    public Router select() {
        return servers[random.nextInt(servers.length)];
    }

}
