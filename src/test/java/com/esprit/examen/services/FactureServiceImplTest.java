package com.esprit.examen.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.esprit.examen.entities.DetailFacture;
import com.esprit.examen.entities.Facture;
import com.esprit.examen.entities.Fournisseur;
import com.esprit.examen.entities.Operateur;
import com.esprit.examen.entities.Produit;
import com.esprit.examen.repositories.DetailFactureRepository;
import com.esprit.examen.repositories.FactureRepository;
import com.esprit.examen.repositories.FournisseurRepository;
import com.esprit.examen.repositories.OperateurRepository;
import com.esprit.examen.repositories.ProduitRepository;

public class FactureServiceImplTest {

    @InjectMocks
    FactureServiceImpl factureService;

    @Mock
    FactureRepository factureRepository;

    @Mock
    OperateurRepository operateurRepository;

    @Mock
    DetailFactureRepository detailFactureRepository;

    @Mock
    FournisseurRepository fournisseurRepository;

    @Mock
    ProduitRepository produitRepository;

    @Mock
    ReglementServiceImpl reglementService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveAllFactures() {
        List<Facture> factures = new ArrayList<>();
        Facture facture1 = new Facture();
        facture1.setIdFacture(1L); // Set attributes after object creation
        facture1.setMontantRemise(50.0f);
        facture1.setMontantFacture(1000.0f);

        Facture facture2 = new Facture();
        facture2.setIdFacture(2L); // Set attributes after object creation
        facture2.setMontantRemise(75.0f);
        facture2.setMontantFacture(1500.0f);

        factures.add(facture1);
        factures.add(facture2);

        when(factureRepository.findAll()).thenReturn(factures);

        List<Facture> result = factureService.retrieveAllFactures();
        assertEquals(2, result.size());
        verify(factureRepository, times(1)).findAll();
    }

    @Test
    public void testAddFacture() {
        Facture facture = new Facture();
        facture.setIdFacture(1L);  // Set attributes after creation
        facture.setMontantRemise(50.0f);
        facture.setMontantFacture(1000.0f);
        facture.setDateCreationFacture(new Date());
        facture.setDateDerniereModificationFacture(new Date());

        when(factureRepository.save(facture)).thenReturn(facture);

        Facture result = factureService.addFacture(facture);
        assertEquals(facture, result);
        verify(factureRepository, times(1)).save(facture);
    }

    @Test
    public void testAddDetailsFacture() {
        Facture facture = new Facture();
        facture.setIdFacture(1L);  // Set attributes after creation
        facture.setMontantFacture(1000.0f);
        facture.setMontantRemise(50.0f);

        DetailFacture detailFacture = new DetailFacture();
        detailFacture.setIdDetailFacture(1L);  // Set attributes after creation
        detailFacture.setQteCommandee(5);
        detailFacture.setPourcentageRemise(10);

        Produit produit = new Produit();
        produit.setIdProduit(1L);  // Set attributes after creation
        produit.setPrix(100.0f);

        detailFacture.setProduit(produit);

        Set<DetailFacture> detailsFacture = new HashSet<>();
        detailsFacture.add(detailFacture);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(detailFactureRepository.save(detailFacture)).thenReturn(detailFacture);

        facture = factureService.addDetailsFacture(facture, detailsFacture);

        assertEquals(450.0f, facture.getMontantFacture());
        assertEquals(50.0f, facture.getMontantRemise());
        verify(produitRepository, times(1)).findById(1L);
        verify(detailFactureRepository, times(1)).save(detailFacture);
    }

    @Test
    public void testCancelFacture() {
        Facture facture = new Facture();
        facture.setIdFacture(1L);  // Set attributes after creation
        facture.setArchivee(false);

        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));

        factureService.cancelFacture(1L);

        assertEquals(true, facture.getArchivee());
        verify(factureRepository, times(1)).save(facture);
        verify(factureRepository, times(1)).updateFacture(1L);
    }

    @Test
    public void testRetrieveFacture() {
        Facture facture = new Facture();
        facture.setIdFacture(1L);  // Set attributes after creation

        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));

        Facture result = factureService.retrieveFacture(1L);
        assertEquals(facture, result);
        verify(factureRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetFacturesByFournisseur() {
        // Create a Fournisseur object
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(1L);  // Set attributes after creation

        // Create a Facture object
        Facture facture = new Facture();
        facture.setIdFacture(1L);  // Set attributes after creation

        // Add Facture to Fournisseur's set of Factures
        Set<Facture> factures = new HashSet<>();
        factures.add(facture);
        fournisseur.setFactures(factures);

        // Mock the repository call to return the Fournisseur with the factures
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(fournisseur));

        // Call the service method and convert the Set to a List
        List<Facture> result = factureService.getFacturesByFournisseur(1L);

        // Assert that the returned list has the expected size
        assertEquals(1, result.size());

        // Verify the repository interaction
        verify(fournisseurRepository, times(1)).findById(1L);
    }



    @Test
    public void testAssignOperateurToFacture() {
        // Create and initialize Operateur object
        Operateur operateur = new Operateur();
        operateur.setIdOperateur(1L);  // Set attributes after creation
        operateur.setNom("Operateur1");

        // Create and initialize Facture object
        Facture facture = new Facture();
        facture.setIdFacture(1L);  // Set attributes after creation

        // Mock the repository calls to return the initialized Operateur and Facture
        when(operateurRepository.findById(1L)).thenReturn(Optional.of(operateur));
        when(factureRepository.findById(1L)).thenReturn(Optional.of(facture));

        // Call the method being tested
        factureService.assignOperateurToFacture(1L, 1L);

        // Ensure that the Facture was added to the Operateur's factures set
        assertEquals(1, operateur.getFactures().size());

        // Verify that the Operateur was saved after being modified
        verify(operateurRepository, times(1)).save(operateur);
    }


    @Test
    public void testPourcentageRecouvrement() {
        Date startDate = new Date();
        Date endDate = new Date();

        when(factureRepository.getTotalFacturesEntreDeuxDates(startDate, endDate)).thenReturn(1000f);
        when(reglementService.getChiffreAffaireEntreDeuxDate(startDate, endDate)).thenReturn(500f);

        float pourcentage = factureService.pourcentageRecouvrement(startDate, endDate);
        assertEquals(50f, pourcentage);
    }
}
