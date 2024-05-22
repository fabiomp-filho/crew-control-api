package br.com.crewcontrolapi.domain.pagination;
import org.springframework.data.domain.Page;

import java.util.List;

public class CustomPageImpl<T> implements CustomPage<T> {
    private final Page<T> page;

    public CustomPageImpl(Page<T> page) {
        this.page = page;
    }

    @Override
    public List<T> getContent() {
        return page.getContent();
    }

    @Override
    public long getTotalElements() {
        return page.getTotalElements();
    }

    @Override
    public int getTotalPages() {
        return page.getTotalPages();
    }

    @Override
    public int getCurrentPage() {
        return page.getNumber();
    }
}
