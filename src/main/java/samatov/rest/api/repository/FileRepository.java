package samatov.rest.api.repository;


import samatov.rest.api.model.File;

import java.util.List;

public interface FileRepository<T, ID> {

    List<File> findAll();

    File findById(Integer id);

    File save(File file);

}
