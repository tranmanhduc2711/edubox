package com.example.edubox.service.impl;

import com.example.edubox.entity.Sequence;
import com.example.edubox.service.SequenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@Service
public class SequenceServiceImpl implements SequenceService {
    public static final String SEQ_NAME_PART = "-";

    @PersistenceContext
    private EntityManager entityManager;

    Map<String, Object> properties = new HashMap<String, Object>() {
        private static final long serialVersionUID = 8568788297889571514L;

        {
            put("javax.persistence.lock.timeout", 5000L);
        }
    };

    @Override
    @Transactional
    public int getNextSeq(String key, Object... args) {
        String seqName = buildSeqName(key, args);
        Sequence confSeq = entityManager.find(Sequence.class, seqName, LockModeType.PESSIMISTIC_WRITE, properties);
        if (confSeq == null) {
            confSeq = new Sequence(seqName, 1);
        } else {
            confSeq.incr(1);
        }
        entityManager.persist(confSeq);

        return confSeq.getCurVal();
    }

    private String buildSeqName(String key, Object[] args) {
        StringBuilder builder = new StringBuilder(key);
        if (args != null) {
            for (Object arg : args) {
                builder.append(SEQ_NAME_PART).append(arg);
            }
        }

        return builder.toString();
    }
}
