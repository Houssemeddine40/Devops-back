

package com.esprit.examen.services;

import com.esprit.examen.entities.Produit;
import com.esprit.examen.entities.Stock;
import com.esprit.examen.repositories.ProduitRepository;
import com.esprit.examen.repositories.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    class ProduitServiceImplTest {

        @InjectMocks
        private ProduitServiceImpl produitService;

        @Mock
        private ProduitRepository produitRepository;

        @Mock
        private StockRepository stockRepository;

        private Produit produit;
        private Stock stock;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            produit = new Produit(1L, "P001", "Produit Test", 19.99f, new Date(), new Date(), null, null, null);
            stock = new Stock(1L, "Stock Test", 100, 10, null);
        }

        @Test
        void testRetrieveAllProduits() {
            List<Produit> produits = new ArrayList<>();
            produits.add(produit);

            when(produitRepository.findAll()).thenReturn(produits);

            List<Produit> retrievedProduits = produitService.retrieveAllProduits();

            assertEquals(1, retrievedProduits.size());
            assertEquals("P001", retrievedProduits.get(0).getCodeProduit());
            verify(produitRepository, times(1)).findAll();
        }

        @Test
        void testAddProduit() {
            when(produitRepository.save(produit)).thenReturn(produit);

            Produit addedProduit = produitService.addProduit(produit);

            assertNotNull(addedProduit);
            assertEquals("P001", addedProduit.getCodeProduit());
            verify(produitRepository, times(1)).save(produit);
        }

        @Test
        void testDeleteProduit() {
            Long produitId = 1L;

            produitService.deleteProduit(produitId);

            verify(produitRepository, times(1)).deleteById(produitId);
        }

        @Test
        void testUpdateProduit() {
            when(produitRepository.save(produit)).thenReturn(produit);

            Produit updatedProduit = produitService.updateProduit(produit);

            assertNotNull(updatedProduit);
            assertEquals("P001", updatedProduit.getCodeProduit());
            verify(produitRepository, times(1)).save(produit);
        }

        @Test
        void testRetrieveProduit() {
            when(produitRepository.findById(produit.getIdProduit())).thenReturn(Optional.of(produit));

            Produit retrievedProduit = produitService.retrieveProduit(produit.getIdProduit());

            assertNotNull(retrievedProduit);
            assertEquals("P001", retrievedProduit.getCodeProduit());
            verify(produitRepository, times(1)).findById(produit.getIdProduit());
        }

        @Test
        void testAssignProduitToStock() {
            when(produitRepository.findById(produit.getIdProduit())).thenReturn(Optional.of(produit));
            when(stockRepository.findById(stock.getIdStock())).thenReturn(Optional.of(stock));

            produitService.assignProduitToStock(produit.getIdProduit(), stock.getIdStock());

            assertEquals(stock, produit.getStock());
            verify(produitRepository, times(1)).save(produit);
        }


        @Test
        void testAssignProduitToStock_WithNonExistentStock() {
            // Arrange
            when(produitRepository.findById(produit.getIdProduit())).thenReturn(Optional.of(produit));
            when(stockRepository.findById(stock.getIdStock())).thenReturn(Optional.empty());

            // Act
            produitService.assignProduitToStock(produit.getIdProduit(), stock.getIdStock());

            // Assert
            verify(produitRepository, times(1)).findById(produit.getIdProduit());
            verify(stockRepository, times(1)).findById(stock.getIdStock());
            verify(produitRepository).save(produit); // save is called even though stock is not found
        }


    }
