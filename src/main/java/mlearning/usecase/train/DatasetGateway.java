package mlearning.usecase;

import mlearning.entity.Dataset;

/** Supplies curated Iris data from the selected source, or throws AccessException.
 * Implementations must never silently substitute a different source.
 */
@FunctionalInterface
public interface DatasetGateway {
    Dataset load(String source);

    class AccessException extends RuntimeException {
        public AccessException(String message, Throwable cause) { super(message, cause); }
    }
}
