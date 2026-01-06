package io.github.upendramanike.dbguard;

/**
 * Pagination helper for database queries.
 */
public final class Pagination {

    private final int page;
    private final int pageSize;
    private final int offset;

    private Pagination(int page, int pageSize) {
        if (page < 1) {
            throw new IllegalArgumentException("Page must be >= 1");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must be >= 1");
        }
        this.page = page;
        this.pageSize = pageSize;
        this.offset = (page - 1) * pageSize;
    }

    /**
     * Creates a new pagination instance.
     *
     * @param page the page number (1-based)
     * @param pageSize the number of items per page
     * @return a new pagination instance
     */
    public static Pagination of(int page, int pageSize) {
        return new Pagination(page, pageSize);
    }

    /**
     * Gets the page number.
     *
     * @return the page number
     */
    public int getPage() {
        return page;
    }

    /**
     * Gets the page size.
     *
     * @return the page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Gets the offset for SQL queries.
     *
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Gets the limit for SQL queries.
     *
     * @return the limit (same as page size)
     */
    public int getLimit() {
        return pageSize;
    }
}

