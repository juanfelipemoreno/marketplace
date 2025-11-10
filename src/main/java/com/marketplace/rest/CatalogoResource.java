package com.marketplace.rest;

import com.marketplace.dto.producto.ProductoCatalogoDTO;
import com.marketplace.enums.TipoProducto;
import com.marketplace.services.interfaces.IProductoService;
import com.marketplace.util.ApiResponse;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * REST Resource para catálogo público de productos
 * @author Felipe Moreno
 */
@Path("/catalogo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CatalogoResource {
    
    @EJB
    private IProductoService productoService;
    
    /**
     * GET /catalogo
     * Listar todos los productos disponibles
     */
    @GET
    public Response listarCatalogo() {
        List<ProductoCatalogoDTO> productos = productoService.listarCatalogo();
        
        ApiResponse<List<ProductoCatalogoDTO>> response = ApiResponse.success(productos);
        response.addMetadata("total", productos.size());
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /catalogo/buscar?q={termino}
     * Buscar productos por término
     */
    @GET
    @Path("/buscar")
    public Response buscar(@QueryParam("q") String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return listarCatalogo();
        }
        
        List<ProductoCatalogoDTO> productos = productoService.buscarProductos(termino);
        
        ApiResponse<List<ProductoCatalogoDTO>> response = ApiResponse.success(productos);
        response.addMetadata("total", productos.size());
        response.addMetadata("termino", termino);
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /catalogo/categoria/{id}
     * Filtrar productos por categoría
     */
    @GET
    @Path("/categoria/{id}")
    public Response filtrarPorCategoria(@PathParam("id") Long idCategoria) {
        List<ProductoCatalogoDTO> productos = productoService.listarPorCategoria(idCategoria);
        
        ApiResponse<List<ProductoCatalogoDTO>> response = ApiResponse.success(productos);
        response.addMetadata("total", productos.size());
        response.addMetadata("categoriaId", idCategoria);
        
        return Response.ok(response).build();
    }
    
    /**
     * GET /catalogo/tipo/{tipo}
     * Filtrar productos por tipo (FISICO o DIGITAL)
     */
    @GET
    @Path("/tipo/{tipo}")
    public Response filtrarPorTipo(@PathParam("tipo") String tipo) {
        try {
            TipoProducto tipoProducto = TipoProducto.valueOf(tipo.toUpperCase());
            List<ProductoCatalogoDTO> productos = productoService.listarPorTipo(tipoProducto);
            
            ApiResponse<List<ProductoCatalogoDTO>> response = ApiResponse.success(productos);
            response.addMetadata("total", productos.size());
            response.addMetadata("tipo", tipo);
            
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            ApiResponse<Object> response = ApiResponse.error(
                "Tipo de producto inválido. Use FISICO o DIGITAL"
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }
    
    /**
     * GET /catalogo/producto/{id}
     * Ver detalle público de un producto
     */
    @GET
    @Path("/producto/{id}")
    public Response verDetalle(@PathParam("id") Long idProducto) {
        ProductoCatalogoDTO producto = productoService.obtenerDetalleCatalogo(idProducto);
        
        ApiResponse<ProductoCatalogoDTO> response = ApiResponse.success(producto);
        return Response.ok(response).build();
    }
}