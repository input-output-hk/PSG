# Configuration file for the Sphinx documentation builder.
#
# This file only contains a selection of the most common options. For a full
# list see the documentation:
# https://www.sphinx-doc.org/en/master/usage/configuration.html

# -- Path setup --------------------------------------------------------------

# If extensions (or modules to document with autodoc) are in another directory,
# add these directories to sys.path here. If the directory is relative to the
# documentation root, use os.path.abspath to make it absolute, like shown here.
#
# import os
# import sys
# sys.path.insert(0, os.path.abspath('.'))

import sys
import os
import sphinx_rtd_theme
import recommonmark

from recommonmark.transform import AutoStructify
from os.path import abspath, join, dirname

sys.path.insert(0, abspath(join(dirname(__file__))))

# -- RTD configuration ------------------------------------------------

on_rtd = os.environ.get("READTHEDOCS", None) == "True"

# This is used for linking and such so we link to the thing we're building
rtd_version = os.environ.get("READTHEDOCS_VERSION", "latest")
if rtd_version not in ["stable", "latest"]:
    rtd_version = "stable"


# -- Project information -----------------------------------------------------

project = 'PSG Services Documentation'
copyright = '2021, IOHK'
author = 'IOHK'

# The full version, including alpha/beta/rc tags
release = '1.0.0'


# -- General configuration ---------------------------------------------------
master_doc = 'index'
# Add any Sphinx extension module names here, as strings. They can be
# extensions coming with Sphinx (named 'sphinx.ext.*') or your custom
# ones.
extensions = [
    "sphinx_rtd_theme",
    'recommonmark',
    'sphinx_markdown_tables',
    'sphinxemoji.sphinxemoji',
    "sphinx.ext.autodoc",
    "sphinx.ext.autosummary",
    "sphinx.ext.intersphinx",
    "sphinx.ext.viewcode",
    'sphinx_panels',
]

# Add any paths that contain templates here, relative to this directory.
templates_path = ['_templates']

# List of patterns, relative to source directory, that match files and
# directories to ignore when looking for source files.
# This pattern also affects html_static_path and html_extra_path.
exclude_patterns = []


# -- Options for HTML output -------------------------------------------------

# The theme to use for HTML and HTML Help pages.  See the documentation for
# a list of builtin themes.
#
html_theme = 'alabaster'

# Add any paths that contain custom static files (such as style sheets) here,
# relative to this directory. They are copied after the builtin static files,
# so a file named "default.css" will overwrite the builtin "default.css".
html_static_path = ['../.sphinx/_static']

source_suffix = {
    '.rst': 'restructuredtext',
    '.md': 'markdown',
}

html_theme = "sphinx_rtd_theme"

html_js_files = [
    'js/hotjar.js',
]

html_theme_options = {
    'logo_only': False,
    'display_version': False,
    'prev_next_buttons_location': 'bottom',
    'style_external_links': False,
    'style_nav_header_background': '#f8f8f5',
    # Toc options
    'collapse_navigation': True,
    'sticky_navigation': True,
    'navigation_depth': 4,
    'includehidden': True,
    'titles_only': False
}

html_css_files = [
    'https://fonts.googleapis.com/css2?family=Chivo:wght@300;400;700&display=swap',
    'css/custom.css',
]

html_logo = "../.sphinx/iohk_full_trans.png"

html_context = {
  "display_github": True, # Add 'Edit on Github' link instead of 'View page source'
  "github_user": "input-output-hk",
  "github_repo": "PSG",
  "github_version": "develop",
  "conf_py_path": "/docs/source/",
  "source_suffix": source_suffix,
}

# -- Custom Document processing ----------------------------------------------

def setup(app):
    app.add_config_value('recommonmark_config', {
            'enable_auto_doc_ref': False,
            'enable_auto_toc_tree': False,
            }, True)
    app.add_transform(AutoStructify)

