package org.innoagencyhack.api.controllers;

import io.jooby.annotations.GET;
import io.jooby.annotations.Path;
import io.jooby.annotations.PathParam;
import org.innoagencyhack.api.logic.RawTextRepository;
import org.innoagencyhack.core.FileInfoModel;

/**
 *
 * @author yurij
 */
@Path("/api/rawtext") 
public class RawTextController {
    @GET
    @Path("/{id}")
    public FileInfoModel get(@PathParam String id) throws Exception {
        try(RawTextRepository repo = new RawTextRepository()) {
            return repo.find(id);
        }
    }
}
