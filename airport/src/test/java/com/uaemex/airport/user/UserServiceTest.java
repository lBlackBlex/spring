package com.uaemex.airport.user;

import com.uaemex.airport.plane.Plane;
import com.uaemex.airport.plane.PlaneRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PlaneRepository planeRepository;
    private UserController userController;

    @BeforeEach
    void setUp() {
        UserService userService = new UserService(userRepository, planeRepository, new BCryptPasswordEncoder());
        userController = new UserController(userService);
    }

    @Test
    void getUser(){
        User user = new User();
        user.setId(UUID.randomUUID());

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        userController.getUser(user.getId());

        verify(userRepository).findById(user.getId());
    }

    @Test
    void getUserThrowException() {
        UUID uuid = UUID.randomUUID();

        assertThatThrownBy(() -> userController.getUser(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User with id " + uuid + " does not exist");
    }

    @Test
    void getUserByEmail(){
        User user = new User();
        user.setEmail("admin@admin.com");

        when(userRepository.findUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        User userByEmail = userController.getUserByEmail(user.getEmail());
        assertEquals(userByEmail.getEmail(), user.getEmail());
    }

    @Test
    void getUserByEmailThrowException() {
        assertThatThrownBy(() -> userController.getUserByEmail(anyString()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User with email does not exist");
    }

    @Test
    void getUsers() {
        userController.getUsers();
        verify(userRepository).findAll();
    }

    @Test
    void createNewUser() {
        User user = new User();
        user.setEmail("admin@admin.com");
        user.setPassword("q");
        user.setName("admin");
        user.setLast_name("adminlast");
        user.setRole("ADMIN");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        userController.registerNewUser(user);

        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertEquals(capturedUser, user);
    }

    @Test
    void addPilotPlane(){
        User pilot = new User();
        pilot.setId(UUID.randomUUID());
        pilot.setRole("PILOT");
        Plane plane = new Plane();
        plane.setId(UUID.randomUUID());
        plane.setPilots(new HashSet<>());

        when(userRepository.findById(pilot.getId()))
                .thenReturn(Optional.of(pilot));
        when(planeRepository.findById(plane.getId()))
                .thenReturn(Optional.of(plane));
        userController.addPilotPlane(pilot.getId(), plane.getId());

        assertNotEquals(plane.getPilots().size(), 0);
    }

    @Test
    void removePilotPlane(){
        User pilot = new User();
        pilot.setId(UUID.randomUUID());
        Plane plane = new Plane();
        plane.setId(UUID.randomUUID());
        plane.setPilots(new HashSet<>());
        pilot.getPlanes().add(plane);
        plane.getPilots().add(pilot);

        when(userRepository.findById(pilot.getId()))
                .thenReturn(Optional.of(pilot));
        when(planeRepository.findById(plane.getId()))
                .thenReturn(Optional.of(plane));
        userController.removePilotPlane(pilot.getId(), plane.getId());

        assertEquals(plane.getPilots().size(), 0);
    }

    @Test
    void updateUser(){
        User user = new User();
        user.setId(UUID.randomUUID());

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        userController.updateUser(
                user.getId(),
                "some@email.com",
                "some name",
                "some last name",
                "EMPLOYEE");
        User userUpdated = userController.getUser(user.getId());

        assertEquals(userUpdated.getEmail(), "some@email.com");
        assertEquals(userUpdated.getName(), "some name");
        assertEquals(userUpdated.getLast_name(), "some last name");
        assertEquals(userUpdated.getRole(), "EMPLOYEE");
    }

    @Test
    void deleteUser(){
        UUID uuid = UUID.randomUUID();

        when(userRepository.existsById(uuid))
                .thenReturn(true);
        userController.deleteUser(uuid);

        verify(userRepository).deleteById(uuid);
    }

    @Test
    void deleteUserThrowException(){
        UUID uuid = UUID.randomUUID();
        assertThatThrownBy(() -> userController.deleteUser(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User with id " + uuid + " does not exist");
    }
}