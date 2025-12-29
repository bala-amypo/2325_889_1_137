package com.example.demo;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import com.example.demo.servlet.SimpleStatusServlet;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.Optional;

@Listeners({TestResultListener.class})
public class SaasUserRolePermissionManagerTest {

    // =====================================================================
    // Mocked dependencies
    // =====================================================================
    @org.mockito.Mock
    private UserAccountRepository userAccountRepository;
    @org.mockito.Mock
    private RoleRepository roleRepository;
    @org.mockito.Mock
    private PermissionRepository permissionRepository;
    @org.mockito.Mock
    private RolePermissionRepository rolePermissionRepository;
    @org.mockito.Mock
    private UserRoleRepository userRoleRepository;
    @org.mockito.Mock
    private AuthenticationManager authenticationManager;
    @org.mockito.Mock
    private PasswordEncoder passwordEncoder;

    // Real JwtUtil
    private JwtUtil jwtUtil;

    // Services
    private AuthService authService;
    private UserAccountService userAccountService;
    private RoleService roleService;
    private PermissionService permissionService;
    private RolePermissionService rolePermissionService;
    private UserRoleService userRoleService;
    private CustomUserDetailsService customUserDetailsService;

    // Servlet
    private SimpleStatusServlet servlet;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil();

        authService = new AuthServiceImpl(
                userAccountRepository,
                passwordEncoder,
                authenticationManager,
                jwtUtil
        );

        userAccountService = new UserAccountServiceImpl(userAccountRepository);
        roleService = new RoleServiceImpl(roleRepository);
        permissionService = new PermissionServiceImpl(permissionRepository);
        rolePermissionService = new RolePermissionServiceImpl(
                rolePermissionRepository,
                roleRepository,
                permissionRepository
        );
        userRoleService = new UserRoleServiceImpl(
                userRoleRepository,
                userAccountRepository,
                roleRepository
        );

        customUserDetailsService = new CustomUserDetailsService(
                userAccountRepository,
                userRoleRepository
        );

        servlet = new SimpleStatusServlet();
    }

    // =====================================================================
    // 1. Servlet tests – "Develop and deploy a simple servlet using Tomcat"
    // =====================================================================

    @Test(priority = 1)
    public void t01_servletReturnsCorrectMessage() throws IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();

        servlet.doGet(req, resp);

        Assert.assertEquals(
                resp.getContentAsString(),
                "SaaS User Role Permission Manager is running"
        );
    }

    @Test(priority = 2)
    public void t02_servletSetsContentTypePlainText() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertEquals(resp.getContentType(), "text/plain");
    }

    @Test(priority = 3)
    public void t03_servletStatusCodeIs200() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertEquals(resp.getStatus(), 200);
    }

    @Test(priority = 4)
    public void t04_servletResponseNotEmpty() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertNotEquals(resp.getContentAsString(), "");
    }

    @Test(priority = 5)
    public void t05_servletResponseContainsProjectName() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertTrue(resp.getContentAsString().contains("SaaS User Role Permission Manager"));
    }

    @Test(priority = 6)
    public void t06_servletMultipleCallsConsistent() throws IOException {
        MockHttpServletResponse r1 = new MockHttpServletResponse();
        MockHttpServletResponse r2 = new MockHttpServletResponse();

        servlet.doGet(new MockHttpServletRequest(), r1);
        servlet.doGet(new MockHttpServletRequest(), r2);

        Assert.assertEquals(r1.getContentAsString(), r2.getContentAsString());
    }

    @Test(priority = 7)
    public void t07_servletWriterAvailable() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertNotNull(resp.getWriter());
    }

    @Test(priority = 8)
    public void t08_servletNoHtmlInResponse() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertFalse(resp.getContentAsString().contains("<html>"));
    }

    @Test(priority = 9)
    public void t09_servletResponseLengthOk() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertTrue(resp.getContentAsString().length() > 20);
    }

    @Test(priority = 10)
    public void t10_servletObjectNotNull() {
        Assert.assertNotNull(servlet);
    }

    // =====================================================================
    // 2. CRUD operations using Spring Boot + REST (Service layer) – 15 tests
    // =====================================================================

    @Test(priority = 11)
    public void t11_createUser_success() {
        UserAccount user = new UserAccount();
        user.setEmail("user@example.com");
        user.setFullName("Test User");

        Mockito.when(userAccountRepository.existsByEmail("user@example.com")).thenReturn(false);
        Mockito.when(userAccountRepository.save(Mockito.any(UserAccount.class)))
                .thenAnswer(inv -> inv.getArgument(0, UserAccount.class));

        UserAccount result = userAccountService.createUser(user);
        Assert.assertEquals(result.getEmail(), "user@example.com");
        Assert.assertTrue(result.isActive());
    }

    @Test(priority = 12)
    public void t12_createUser_duplicateEmail() {
        UserAccount user = new UserAccount();
        user.setEmail("dup@example.com");

        Mockito.when(userAccountRepository.existsByEmail("dup@example.com")).thenReturn(true);
        Assert.expectThrows(BadRequestException.class, () -> userAccountService.createUser(user));
    }

    @Test(priority = 13)
    public void t13_updateUser_success() {
        UserAccount existing = new UserAccount();
        existing.setId(1L);
        existing.setEmail("old@example.com");
        existing.setFullName("Old Name");

        UserAccount update = new UserAccount();
        update.setEmail("new@example.com");
        update.setFullName("New Name");

        Mockito.when(userAccountRepository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(userAccountRepository.save(Mockito.any(UserAccount.class)))
                .thenAnswer(inv -> inv.getArgument(0, UserAccount.class));

        UserAccount result = userAccountService.updateUser(1L, update);
        Assert.assertEquals(result.getEmail(), "new@example.com");
        Assert.assertEquals(result.getFullName(), "New Name");
    }

    @Test(priority = 14)
    public void t14_updateUser_notFound() {
        Mockito.when(userAccountRepository.findById(99L)).thenReturn(Optional.empty());
        Assert.expectThrows(ResourceNotFoundException.class,
                () -> userAccountService.updateUser(99L, new UserAccount()));
    }

    @Test(priority = 15)
    public void t15_getUserById_success() {
        UserAccount user = new UserAccount();
        user.setId(10L);
        user.setEmail("u10@example.com");
        Mockito.when(userAccountRepository.findById(10L)).thenReturn(Optional.of(user));

        UserAccount result = userAccountService.getUserById(10L);
        Assert.assertEquals(result.getId(), Long.valueOf(10L));
    }

    @Test(priority = 16)
    public void t16_getUserById_notFound() {
        Mockito.when(userAccountRepository.findById(123L)).thenReturn(Optional.empty());
        Assert.expectThrows(ResourceNotFoundException.class,
                () -> userAccountService.getUserById(123L));
    }

    @Test(priority = 17)
    public void t17_deactivateUser_success() {
        UserAccount user = new UserAccount();
        user.setId(5L);
        user.setEmail("active@example.com");
        user.setActive(true);

        Mockito.when(userAccountRepository.findById(5L)).thenReturn(Optional.of(user));
        Mockito.when(userAccountRepository.save(Mockito.any(UserAccount.class)))
                .thenAnswer(inv -> inv.getArgument(0, UserAccount.class));

        userAccountService.deactivateUser(5L);
        Assert.assertFalse(user.isActive());
    }

    @Test(priority = 18)
    public void t18_createRole_success() {
        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");
        Mockito.when(roleRepository.findByRoleName("ROLE_ADMIN"))
                .thenReturn(Optional.empty());
        Mockito.when(roleRepository.save(Mockito.any(Role.class)))
                .thenAnswer(inv -> inv.getArgument(0, Role.class));

        Role result = roleService.createRole(role);
        Assert.assertEquals(result.getRoleName(), "ROLE_ADMIN");
        Assert.assertTrue(result.isActive());
    }

    @Test(priority = 19)
    public void t19_createRole_duplicateName() {
        Role existing = new Role();
        existing.setRoleName("ROLE_ADMIN");
        Mockito.when(roleRepository.findByRoleName("ROLE_ADMIN"))
                .thenReturn(Optional.of(existing));

        Role newRole = new Role();
        newRole.setRoleName("ROLE_ADMIN");

        Assert.expectThrows(BadRequestException.class, () -> roleService.createRole(newRole));
    }

    @Test(priority = 20)
    public void t20_updateRole_success() {
        Role existing = new Role();
        existing.setId(1L);
        existing.setRoleName("ROLE_USER");
        existing.setDescription("Old");

        Role updated = new Role();
        updated.setRoleName("ROLE_MANAGER");
        updated.setDescription("Updated");

        Mockito.when(roleRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        Mockito.when(roleRepository.save(Mockito.any(Role.class)))
                .thenAnswer(inv -> inv.getArgument(0, Role.class));

        Role result = roleService.updateRole(1L, updated);
        Assert.assertEquals(result.getRoleName(), "ROLE_MANAGER");
        Assert.assertEquals(result.getDescription(), "Updated");
    }

    @Test(priority = 21)
    public void t21_deactivateRole_success() {
        Role role = new Role();
        role.setId(2L);
        role.setRoleName("ROLE_TEST");
        role.setActive(true);

        Mockito.when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.save(Mockito.any(Role.class)))
                .thenAnswer(inv -> inv.getArgument(0, Role.class));

        roleService.deactivateRole(2L);
        Assert.assertFalse(role.isActive());
    }

    @Test(priority = 22)
    public void t22_createPermission_success() {
        Permission perm = new Permission();
        perm.setPermissionKey("READ_USER");

        Mockito.when(permissionRepository.findByPermissionKey("READ_USER"))
                .thenReturn(Optional.empty());
        Mockito.when(permissionRepository.save(Mockito.any(Permission.class)))
                .thenAnswer(inv -> inv.getArgument(0, Permission.class));

        Permission result = permissionService.createPermission(perm);
        Assert.assertEquals(result.getPermissionKey(), "READ_USER");
        Assert.assertTrue(result.isActive());
    }

    @Test(priority = 23)
    public void t23_createPermission_duplicateKey() {
        Permission existing = new Permission();
        existing.setPermissionKey("WRITE_POST");
        Mockito.when(permissionRepository.findByPermissionKey("WRITE_POST"))
                .thenReturn(Optional.of(existing));

        Permission perm = new Permission();
        perm.setPermissionKey("WRITE_POST");

        Assert.expectThrows(BadRequestException.class,
                () -> permissionService.createPermission(perm));
    }

    @Test(priority = 24)
    public void t24_deactivatePermission_success() {
        Permission perm = new Permission();
        perm.setId(3L);
        perm.setPermissionKey("DELETE_FILE");
        perm.setActive(true);

        Mockito.when(permissionRepository.findById(3L))
                .thenReturn(Optional.of(perm));
        Mockito.when(permissionRepository.save(Mockito.any(Permission.class)))
                .thenAnswer(inv -> inv.getArgument(0, Permission.class));

        permissionService.deactivatePermission(3L);
        Assert.assertFalse(perm.isActive());
    }

    @Test(priority = 25)
    public void t25_getAllUsers_returnsList() {
        Mockito.when(userAccountRepository.findAll())
                .thenReturn(Arrays.asList(new UserAccount(), new UserAccount()));
        List<UserAccount> list = userAccountService.getAllUsers();
        Assert.assertEquals(list.size(), 2);
    }

    // =====================================================================
    // 3. Dependency Injection & IoC tests – 5 tests
    // =====================================================================

    @Test(priority = 26)
    public void t26_di_authServiceNotNull() {
        Assert.assertNotNull(authService);
    }

    @Test(priority = 27)
    public void t27_di_userAccountServiceNotNull() {
        Assert.assertNotNull(userAccountService);
    }

    @Test(priority = 28)
    public void t28_di_roleServiceNotNull() {
        Assert.assertNotNull(roleService);
    }

    @Test(priority = 29)
    public void t29_di_permissionServiceNotNull() {
        Assert.assertNotNull(permissionService);
    }

    @Test(priority = 30)
    public void t30_di_userRoleServiceNotNull() {
        Assert.assertNotNull(userRoleService);
    }

    // =====================================================================
    // 4. Hibernate configs, generator classes & CRUD – 8 tests
    // =====================================================================

    @Test(priority = 31)
    public void t31_userAccount_prePersistSetsTimestamps() {
        UserAccount user = new UserAccount();
        user.setEmail("pre@example.com");
        user.prePersist();
        Assert.assertNotNull(user.getCreatedAt());
        Assert.assertNotNull(user.getUpdatedAt());
    }

    @Test(priority = 32)
    public void t32_userAccount_preUpdateUpdatesTimestamp() {
        UserAccount user = new UserAccount();
        user.setEmail("pre2@example.com");
        user.prePersist();
        Instant first = user.getUpdatedAt();
        user.preUpdate();
        Instant second = user.getUpdatedAt();
        Assert.assertTrue(second.isAfter(first) || second.equals(first));
    }

    @Test(priority = 33)
    public void t33_rolePermission_prePersistSetsGrantedAt() {
        RolePermission rp = new RolePermission();
        rp.prePersist();
        Assert.assertNotNull(rp.getGrantedAt());
    }

    @Test(priority = 34)
    public void t34_userRole_prePersistSetsAssignedAt() {
        UserRole ur = new UserRole();
        ur.prePersist();
        Assert.assertNotNull(ur.getAssignedAt());
    }

    @Test(priority = 35)
    public void t35_role_defaultActiveTrue() {
        Role r = new Role();
        r.setRoleName("ROLE_SAMPLE");
        Assert.assertTrue(r.isActive());
    }

    @Test(priority = 36)
    public void t36_permission_defaultActiveTrue() {
        Permission p = new Permission();
        p.setPermissionKey("SAMPLE");
        Assert.assertTrue(p.isActive());
    }

    @Test(priority = 37)
    public void t37_userAccount_defaultActiveTrue() {
        UserAccount u = new UserAccount();
        u.setEmail("act@example.com");
        Assert.assertTrue(u.isActive());
    }

    @Test(priority = 38)
    public void t38_rolePermission_hasRoleAndPermission() {
        Role r = new Role();
        Permission p = new Permission();
        RolePermission rp = new RolePermission();
        rp.setRole(r);
        rp.setPermission(p);

        Assert.assertEquals(rp.getRole(), r);
        Assert.assertEquals(rp.getPermission(), p);
    }

    // =====================================================================
    // 5. JPA mapping & normalization (1NF,2NF,3NF via mapping entities) – 5 tests
    // =====================================================================

    @Test(priority = 39)
    public void t39_userRole_userMappedCorrectly() {
        UserAccount u = new UserAccount();
        u.setEmail("map@example.com");
        Role r = new Role();
        r.setRoleName("ROLE_A");

        UserRole ur = new UserRole();
        ur.setUser(u);
        ur.setRole(r);

        Assert.assertEquals(ur.getUser(), u);
        Assert.assertEquals(ur.getRole(), r);
    }

    @Test(priority = 40)
    public void t40_rolePermission_roleMappedCorrectly() {
        Role r = new Role();
        Permission p = new Permission();
        RolePermission rp = new RolePermission();
        rp.setRole(r);
        rp.setPermission(p);

        Assert.assertEquals(rp.getRole(), r);
        Assert.assertEquals(rp.getPermission(), p);
    }

    @Test(priority = 41)
    public void t41_userAccount_normalizedEmailField() {
        UserAccount u = new UserAccount();
        u.setEmail("norm@example.com");
        Assert.assertEquals(u.getEmail(), "norm@example.com");
    }

    @Test(priority = 42)
    public void t42_role_uniqueRoleNameConcept() {
        Role role = new Role();
        role.setRoleName("ROLE_UNIQUE");
        Assert.assertEquals(role.getRoleName(), "ROLE_UNIQUE");
    }

    @Test(priority = 43)
    public void t43_permission_uniqueKeyConcept() {
        Permission p = new Permission();
        p.setPermissionKey("READ_SOMETHING");
        Assert.assertEquals(p.getPermissionKey(), "READ_SOMETHING");
    }

    // =====================================================================
    // 6. Many-to-Many relationships & association tests – 5 tests
    // =====================================================================

    @Test(priority = 44)
    public void t44_userCanHaveMultipleRolesViaUserRole() {
        UserAccount u = new UserAccount();
        u.setId(1L);

        Role r1 = new Role();
        r1.setId(1L);
        r1.setRoleName("ROLE_ADMIN");

        Role r2 = new Role();
        r2.setId(2L);
        r2.setRoleName("ROLE_EDITOR");

        UserRole ur1 = new UserRole();
        ur1.setUser(u);
        ur1.setRole(r1);

        UserRole ur2 = new UserRole();
        ur2.setUser(u);
        ur2.setRole(r2);

        List<UserRole> roles = Arrays.asList(ur1, ur2);
        Assert.assertEquals(roles.size(), 2);
    }

    @Test(priority = 45)
    public void t45_roleCanHaveMultiplePermissionsViaRolePermission() {
        Role r = new Role();
        r.setId(10L);

        Permission p1 = new Permission();
        p1.setId(1L);
        Permission p2 = new Permission();
        p2.setId(2L);

        RolePermission rp1 = new RolePermission();
        rp1.setRole(r);
        rp1.setPermission(p1);

        RolePermission rp2 = new RolePermission();
        rp2.setRole(r);
        rp2.setPermission(p2);

        List<RolePermission> list = Arrays.asList(rp1, rp2);
        Assert.assertEquals(list.size(), 2);
    }

    @Test(priority = 46)
    public void t46_assignRole_serviceSuccess() {
        UserAccount u = new UserAccount();
        u.setId(1L);
        u.setActive(true);

        Role r = new Role();
        r.setId(2L);
        r.setActive(true);

        UserRole mapping = new UserRole();
        mapping.setUser(u);
        mapping.setRole(r);

        Mockito.when(userAccountRepository.findById(1L)).thenReturn(Optional.of(u));
        Mockito.when(roleRepository.findById(2L)).thenReturn(Optional.of(r));
        Mockito.when(userRoleRepository.save(Mockito.any(UserRole.class)))
                .thenAnswer(inv -> inv.getArgument(0, UserRole.class));

        UserRole result = userRoleService.assignRole(mapping);
        Assert.assertEquals(result.getUser(), u);
        Assert.assertEquals(result.getRole(), r);
    }

    @Test(priority = 47)
    public void t47_assignRole_inactiveUserFails() {
        UserAccount u = new UserAccount();
        u.setId(1L);
        u.setActive(false);
        Role r = new Role();
        r.setId(2L);
        r.setActive(true);

        UserRole mapping = new UserRole();
        mapping.setUser(u);
        mapping.setRole(r);

        Mockito.when(userAccountRepository.findById(1L)).thenReturn(Optional.of(u));
        Mockito.when(roleRepository.findById(2L)).thenReturn(Optional.of(r));

        Assert.expectThrows(BadRequestException.class,
                () -> userRoleService.assignRole(mapping));
    }

    @Test(priority = 48)
    public void t48_assignRole_inactiveRoleFails() {
        UserAccount u = new UserAccount();
        u.setId(1L);
        u.setActive(true);
        Role r = new Role();
        r.setId(2L);
        r.setActive(false);

        UserRole mapping = new UserRole();
        mapping.setUser(u);
        mapping.setRole(r);

        Mockito.when(userAccountRepository.findById(1L)).thenReturn(Optional.of(u));
        Mockito.when(roleRepository.findById(2L)).thenReturn(Optional.of(r));

        Assert.expectThrows(BadRequestException.class,
                () -> userRoleService.assignRole(mapping));
    }

    // =====================================================================
    // 7. Security & JWT token-based authentication – 9 tests
    // =====================================================================

    @Test(priority = 49)
    public void t49_jwt_generateToken_success() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1L);
        claims.put("email", "jwt@example.com");
        String token = jwtUtil.generateToken(claims, "jwt@example.com");
        Assert.assertNotNull(token);
    }

    @Test(priority = 50)
    public void t50_jwt_extractUsername_success() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1L);
        String token = jwtUtil.generateToken(claims, "sub@example.com");

        String username = jwtUtil.getUsername(token);
        Assert.assertEquals(username, "sub@example.com");
    }

    @Test(priority = 51)
    public void t51_jwt_expirationMillisPositive() {
        Assert.assertTrue(jwtUtil.getExpirationMillis() > 0);
    }

    @Test(priority = 52)
    public void t52_jwt_tokenValidForCorrectUsername() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1L);
        String token = jwtUtil.generateToken(claims, "user@example.com");

        boolean valid = jwtUtil.isTokenValid(token, "user@example.com");
        Assert.assertEquals(valid, true);
    }

    @Test(priority = 53)
    public void t53_jwt_tokenInvalidForDifferentUsername() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1L);
        String token = jwtUtil.generateToken(claims, "real@example.com");

        boolean valid = jwtUtil.isTokenValid(token, "other@example.com");
        Assert.assertEquals(valid, false);
    }

    @Test(priority = 54)
    public void t54_customUserDetails_loadUserSuccess() {
        UserAccount user = new UserAccount();
        user.setId(1L);
        user.setEmail("login@example.com");
        user.setActive(true);

        Role role = new Role();
        role.setId(2L);
        role.setRoleName("ROLE_ADMIN");

        UserRole mapping = new UserRole();
        mapping.setUser(user);
        mapping.setRole(role);

        Mockito.when(userAccountRepository.findByEmail("login@example.com"))
                .thenReturn(Optional.of(user));
        Mockito.when(userRoleRepository.findByUser_Id(1L))
                .thenReturn(Collections.singletonList(mapping));

        UserDetails details = customUserDetailsService.loadUserByUsername("login@example.com");
        Assert.assertEquals(details.getUsername(), "login@example.com");
        Assert.assertEquals(details.getAuthorities().size(), 1);
    }

    @Test(priority = 55)
    public void t55_customUserDetails_userNotFound() {
        Mockito.when(userAccountRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        Assert.expectThrows(Exception.class,
                () -> customUserDetailsService.loadUserByUsername("missing@example.com"));
    }

    @Test(priority = 56)
    public void t56_authService_loginSuccessWithFixedPassword() {
        AuthRequestDto request = new AuthRequestDto();
        request.setEmail("auth@example.com");
        request.setPassword("password"); // fixed password for Option A

        UserAccount user = new UserAccount();
        user.setId(1L);
        user.setEmail("auth@example.com");
        user.setActive(true);

        Mockito.when(authenticationManager.authenticate(
                Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        Mockito.when(userAccountRepository.findByEmail("auth@example.com"))
                .thenReturn(Optional.of(user));

        AuthResponseDto response = authService.login(request);
        Assert.assertNotNull(response.getToken());
    }

    @Test(priority = 57)
    public void t57_authService_registerDuplicateEmailFails() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("dup@example.com");
        dto.setPassword("password123");

        Mockito.when(userAccountRepository.existsByEmail("dup@example.com"))
                .thenReturn(true);

        Assert.expectThrows(BadRequestException.class,
                () -> authService.register(dto));
    }

    // =====================================================================
    // 8. HQL / HCQL-style repository & service queries – 13 tests
    // =====================================================================

    @Test(priority = 58)
    public void t58_repo_findUserByEmail() {
        UserAccount user = new UserAccount();
        user.setEmail("query@example.com");
        Mockito.when(userAccountRepository.findByEmail("query@example.com"))
                .thenReturn(Optional.of(user));

        Optional<UserAccount> result = userAccountRepository.findByEmail("query@example.com");
        Assert.assertTrue(result.isPresent());
    }

    @Test(priority = 59)
    public void t59_repo_findRoleByRoleName() {
        Role r = new Role();
        r.setRoleName("ROLE_QUERY");
        Mockito.when(roleRepository.findByRoleName("ROLE_QUERY"))
                .thenReturn(Optional.of(r));

        Optional<Role> result = roleRepository.findByRoleName("ROLE_QUERY");
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getRoleName(), "ROLE_QUERY");
    }

    @Test(priority = 60)
    public void t60_repo_findPermissionByKey() {
        Permission p = new Permission();
        p.setPermissionKey("READ_QUERY");
        Mockito.when(permissionRepository.findByPermissionKey("READ_QUERY"))
                .thenReturn(Optional.of(p));

        Optional<Permission> result = permissionRepository.findByPermissionKey("READ_QUERY");
        Assert.assertTrue(result.isPresent());
    }

    @Test(priority = 61)
    public void t61_repo_findRolePermissionsByRoleId() {
        Mockito.when(rolePermissionRepository.findByRole_Id(5L))
                .thenReturn(Arrays.asList(new RolePermission(), new RolePermission()));
        List<RolePermission> list = rolePermissionRepository.findByRole_Id(5L);
        Assert.assertEquals(list.size(), 2);
    }

    @Test(priority = 62)
    public void t62_repo_findUserRolesByUserId() {
        Mockito.when(userRoleRepository.findByUser_Id(7L))
                .thenReturn(Arrays.asList(new UserRole(), new UserRole(), new UserRole()));

        List<UserRole> list = userRoleRepository.findByUser_Id(7L);
        Assert.assertEquals(list.size(), 3);
    }

    @Test(priority = 63)
    public void t63_rolePermissionService_getPermissionsForRole() {
        Mockito.when(rolePermissionRepository.findByRole_Id(9L))
                .thenReturn(Arrays.asList(new RolePermission(), new RolePermission()));

        List<RolePermission> list = rolePermissionService.getPermissionsForRole(9L);
        Assert.assertEquals(list.size(), 2);
    }

    @Test(priority = 64)
    public void t64_rolePermissionService_getMappingById_success() {
        RolePermission rp = new RolePermission();
        rp.setId(1L);
        Mockito.when(rolePermissionRepository.findById(1L))
                .thenReturn(Optional.of(rp));

        RolePermission result = rolePermissionService.getMappingById(1L);
        Assert.assertEquals(result.getId(), Long.valueOf(1L));
    }

    @Test(priority = 65)
    public void t65_rolePermissionService_getMappingById_notFound() {
        Mockito.when(rolePermissionRepository.findById(99L))
                .thenReturn(Optional.empty());
        Assert.expectThrows(ResourceNotFoundException.class,
                () -> rolePermissionService.getMappingById(99L));
    }

    @Test(priority = 66)
    public void t66_userRoleService_getRolesForUser() {
        Mockito.when(userRoleRepository.findByUser_Id(11L))
                .thenReturn(Arrays.asList(new UserRole()));

        List<UserRole> list = userRoleService.getRolesForUser(11L);
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 67)
    public void t67_userRoleService_getMappingById_success() {
        UserRole ur = new UserRole();
        ur.setId(2L);
        Mockito.when(userRoleRepository.findById(2L))
                .thenReturn(Optional.of(ur));

        UserRole result = userRoleService.getMappingById(2L);
        Assert.assertEquals(result.getId(), Long.valueOf(2L));
    }

    @Test(priority = 68)
    public void t68_userRoleService_getMappingById_notFound() {
        Mockito.when(userRoleRepository.findById(200L))
                .thenReturn(Optional.empty());
        Assert.expectThrows(ResourceNotFoundException.class,
                () -> userRoleService.getMappingById(200L));
    }

    @Test(priority = 69)
    public void t69_userRoleService_removeRole_success() {
        Mockito.when(userRoleRepository.existsById(3L)).thenReturn(true);
        userRoleService.removeRole(3L);
        Mockito.verify(userRoleRepository, Mockito.times(1)).deleteById(3L);
    }

    @Test(priority = 70)
    public void t70_userRoleService_removeRole_notFound() {
        Mockito.when(userRoleRepository.existsById(300L)).thenReturn(false);
        Assert.expectThrows(ResourceNotFoundException.class,
                () -> userRoleService.removeRole(300L));
    }
}
