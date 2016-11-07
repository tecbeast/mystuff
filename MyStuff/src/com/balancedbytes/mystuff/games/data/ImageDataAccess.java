package com.balancedbytes.mystuff.games.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestDataAccess;
import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.Image;
import com.balancedbytes.mystuff.games.Images;

public class ImageDataAccess extends RestDataAccess<Image> {

	private static final String _SQL_FIND_ALL_IMAGES = 
		"SELECT * FROM images ORDER BY role";
	private static final String _SQL_FIND_IMAGE_BY_ID =
		"SELECT * FROM images WHERE id = ?";
	private static final String _SQL_FIND_IMAGES_BY_GAME_ID =
		"SELECT game_images.game_id, images.*"
		+ " FROM game_images LEFT JOIN images"
		+ " ON game_images.image_id = images.id"
		+ " WHERE game_images.game_id = ?"
		+ " ORDER BY images.role";
	private static final String _SQL_CREATE_IMAGE =
		"INSERT INTO images"
		+ " (role, width, height, url, description)"
		+ " VALUES (?, ?, ?, ?, ?)";
	private static final String _SQL_UPDATE_IMAGE =
		"UPDATE images"
		+ " SET role = ?, width = ?, height = ?, url = ?, description = ?"
		+ " WHERE id = ?";
	private static final String _SQL_DELETE_IMAGE =
		"DELETE FROM images WHERE id = ?";

    public Images findAllImages(RestDataPaging paging) throws SQLException {
    	Images images = new Images();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_ALL_IMAGES);
            processResultSet(ps.executeQuery(), images, paging);
		}
        return images;
    }

    public Image findImageById(String id) throws SQLException {
    	Image image = null;
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_IMAGE_BY_ID);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            image = processResultSet(ps.executeQuery());
		}
        return image;
    }
    
    public Images findImagesByGameId(String gameId, RestDataPaging paging) throws SQLException {
    	Images images = new Images();
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(_SQL_FIND_IMAGES_BY_GAME_ID);
            ps.setLong(1, MyStuffUtil.parseLong(gameId));
            processResultSet(ps.executeQuery(), images, paging);
		}
        return images;
    }

    public void createImage(Image image) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_CREATE_IMAGE, new String[] { "id" });
            ps.setString(1, image.getRole());
            ps.setInt(2, image.getWidth());
            ps.setInt(3, image.getHeight());
            ps.setString(4, image.getUrl());
            ps.setString(5, image.getDescription());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
            	image.setId(rs.getString(1));
            }
        }
    }
    
    public boolean updateImage(Image image) throws SQLException {
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_UPDATE_IMAGE);
            ps.setString(1, image.getRole());
            ps.setInt(2, image.getWidth());
            ps.setInt(3, image.getHeight());
            ps.setString(4, image.getUrl());
            ps.setString(5, image.getDescription());
            ps.setLong(6, MyStuffUtil.parseLong(image.getId()));
            return (ps.executeUpdate() == 1);
        }
    }
    
    public boolean deleteImage(String id) throws SQLException {
    	int count = 0;
        try (Connection c = ConnectionHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(_SQL_DELETE_IMAGE);
            ps.setLong(1, MyStuffUtil.parseLong(id));
            count = ps.executeUpdate();
        }
        return (count == 1);
    }

    @Override
    protected Image processRow(ResultSet rs) throws SQLException {
    	Image image = new Image();
    	image.setId(rs.getString("id"));
    	image.setRole(rs.getString("role"));
    	image.setWidth(rs.getInt("width"));
    	image.setHeight(rs.getInt("height"));
    	image.setUrl(rs.getString("url"));
    	image.setDescription(rs.getString("description"));
        return image;
    }
    
}
