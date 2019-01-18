//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.ymdd.container.runtime.auth;

import java.util.function.Supplier;
import cn.ymdd.container.runtime.auth.AuthoritySession;
public final class AuthorityContext {
    private static ThreadLocal<AuthorityContext> authorityHolder = ThreadLocal.withInitial(() -> {
        return new AuthorityContext();
    });
    private AuthoritySession authoritySession;

    public AuthorityContext() {
    }

    public static boolean hasAuthoritySession() {
        return ((AuthorityContext)authorityHolder.get()).authoritySession != null;
    }

    public static AuthoritySession getAuthoritySession() {
        return ((AuthorityContext)authorityHolder.get()).authoritySession;
    }

    public static void setAuthoritySession(AuthoritySession authoritySession) {
        ((AuthorityContext)authorityHolder.get()).authoritySession = authoritySession;
    }

    public static AuthorityContext getAuthorityContext() {
        return (AuthorityContext)authorityHolder.get();
    }

    public static void delAuthorityContext() {
        authorityHolder.remove();
    }
}
