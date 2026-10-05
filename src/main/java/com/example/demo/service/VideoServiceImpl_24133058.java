package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dao.VideoDao_24133058;
import com.example.demo.model.Category_24133058;
import com.example.demo.model.Video_24133058;

@Service
public class VideoServiceImpl_24133058 implements VideoService_24133058 {

    @Autowired
    private VideoDao_24133058 videoDao;

    @Override
    public Video_24133058 findById(String videoId) {
        return videoDao.findById(videoId);
    }

    @Override
    public List<Video_24133058> findByCategoryIdPage(int categoryId, int page, int pageSize) {
        return videoDao.findByCategoryIdPage(categoryId, page, pageSize);
    }

    @Override
    public int countByCategoryId(int categoryId) {
        return videoDao.countByCategoryId(categoryId);
    }

    @Override
    public List<Category_24133058> findAllCategoriesWithCount() {
        return videoDao.findAllCategoriesWithCount();
    }

    @Override
    public Category_24133058 findCategoryById(int categoryId) {
        return videoDao.findCategoryById(categoryId);
    }
}