package com.trigon.exceptions;

import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Maps;

import java.util.Map;

public class TrigonAsserts extends SoftAssert {
    private static final String DEFAULT_SOFT_ASSERT_MESSAGE = "The following asserts failed:";
    // LinkedHashMap to preserve the order
    private final Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();

    @Override
    protected void doAssert(IAssert<?> a) {
        onBeforeAssert(a);
        try {
            a.doAssert();
            onAssertSuccess(a);
        } catch (AssertionError ex) {
            onAssertFailure(a, ex);
            m_errors.put(ex, a);
        } finally {
            onAfterAssert(a);
        }
    }

    @Override
    public void assertAll() {
        assertAll(null);
    }

    @Override
    public void assertAll(String message) {
        if (!m_errors.isEmpty()) {
            StringBuilder sb = new StringBuilder(null == message ? DEFAULT_SOFT_ASSERT_MESSAGE : message);
            boolean first = true;
            for (AssertionError error : m_errors.keySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append("\n\t");
                sb.append(getErrorDetails(error));
            }
            throw new AssertionError(sb.toString());
        }
    }
}
